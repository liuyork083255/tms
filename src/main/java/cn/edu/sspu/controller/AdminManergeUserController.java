package cn.edu.sspu.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.sspu.dao.mapper.User_TableMapper;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.EmailModel;
import cn.edu.sspu.models.File;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.User;
import cn.edu.sspu.models.User_Table;
import cn.edu.sspu.pojo.Json;
import cn.edu.sspu.service.FileService;
import cn.edu.sspu.service.InputService;
import cn.edu.sspu.service.TableService;
import cn.edu.sspu.service.UserService;
import cn.edu.sspu.utils.AdminUtils;
import cn.edu.sspu.utils.MailUtils;

@Controller
@RequestMapping("/adminManergeUserController")
public class AdminManergeUserController {
	@Autowired
	private UserService userService;
    @Autowired  
    private MailUtils mailUtil;
    @Autowired
    private TableService tableService;
    @Autowired
    private InputService inputService;
    @Autowired
    private FileService fileService;
    
    private static Logger logger = LoggerFactory.getLogger(AdminManergeUserController.class);
	
	@ResponseBody
	@RequestMapping("/getAllUser")
	public Map<String,Object> getAllUser(String name,int page,int rows) throws ServiceException{
		// 1 获得table总记录数
		int userTotal = userService.getUserTotal(name);
		// 2 获得table集合
		List<User> userList = userService.selectUserByFilter(name, (page-1)*rows, rows);
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total", userTotal);//---------------------------------这里要查询真实数据库中的总记录

		map.put("rows", userList);
		return map;
	}
	
	
	/**
	 * 这个方法有点特殊，因为前台触发保存按钮，这个用户可能是新用户，也可能是已有用户
	 * 所以这里要判断，如果是新用户，就直接调用保存，已有用户就直接更新
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateUser")
	public Json updateUser(@RequestBody User user){
		Json json = new Json();
		if(user == null){
			json.setMsg("获取user参数失败");
			json.setSuccess(false);
			return json;
		}
		
		//如果user_id为null，说明是新增用户
		if(user.getUser_id() == null){
			user.setUser_id(AdminUtils.getUUID());
			int m = 0;
			try {
				m = userService.insertUser(user);
			} catch (ServiceException e) {
				logger.error(e.getMessage());
				json.setMsg("新增失败");
				json.setSuccess(false);
				return json;
			}
			json.setMsg("新增成功");
			json.setSuccess(true);
			return json;
		}
		

		//校验用户名是否有效
		boolean flag = validateUserName(user.getName(),user.getUser_id());
		if(flag){
			json.setMsg("用户名重复");
			json.setSuccess(false);
			return json;
		}
		
		
		int n = 0;
		try {
			n = userService.updateUser(user);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		if(n == 0){
			json.setMsg("保存失败，异常未知");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("保存成功");
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/deleteUserById")
	public Json deleteUserById(String user_id){
		Json json = new Json();
		if(user_id == null){
			json.setMsg("获取user_id失败");
			json.setSuccess(false);
			return json;
		}
		int n = 0;
		try {
			n = userService.deleteUserById(user_id);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		if(n == 0){
			json.setMsg("删除失败，异常未知");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("删除成功");
		json.setSuccess(true);
		return json;
	}
	
	
	@ResponseBody
	@RequestMapping("/sendEmail")
	public Json sendEmail(@RequestBody EmailModel emailModel){
		Json json = new Json();
		if(emailModel == null || emailModel.getMyEmail() == null || emailModel.getText() == null || emailModel.getToEmails() == null){
			json.setMsg("封装emailModel参数失败");
			json.setSuccess(false);
			return json;
		}
		try{
		mailUtil.send(emailModel.getToEmails(), "", emailModel.getText());
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setMsg("发送失败");
			json.setSuccess(true);
			return json;
		}
		json.setMsg("发送成功");
		json.setSuccess(true);
		return json;
	}
	
	
	
	/**
	 * 判断指定用户名是否为空
	 * true  不为空
	 * false 为空
	 * @param name
	 * @return
	 */
	public boolean validateUserName(String name,String user_id){
		User user = null;
		user = userService.selectUserByIdNotName(user_id,name);
		if(user == null) return false;
		else return true;
	}
	
	@ResponseBody
	@RequestMapping("/getAllTableIdAndName")
	public List<Object> getAllTableIdAndName(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<Object> objectList = null;
		objectList = tableService.getAllTableIdAndName();
		if(objectList == null || objectList.size() == 0){
			request.setAttribute("msg", "获取session中的User失败，请重新登录");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		return objectList;
	}
	
	@ResponseBody
	@RequestMapping("/getTableByTableId")
	public Json getTableByTableId(String table_id){
		Json json = new Json();
		if(table_id == null){
			json.setMsg("获取table_id参数失败");
			json.setSuccess(false);
			return json;
		}
		List<Input> inputList = null;
		try {
			inputList = inputService.selectInputByTableId(table_id);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg("获取inputList位置异常");
			json.setSuccess(false);
			return json;
		}
		json.setObj(inputList);
		json.setSuccess(true);
		return json;
	}
	
	
	@ResponseBody
	@RequestMapping("/getAllUserIdAndName")
	public List<Object> getAllUserIdAndName(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<Object> objectList = null;
		objectList = userService.getAllUserIdAndName();
		if(objectList == null || objectList.size() == 0){
			request.setAttribute("msg", "获取session中的User失败，请重新登录");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		return objectList;
	}
	
	
	
	
	/**
	 * 这个方法是获取指定 的table和user的所有填写的表记录，user可能为空
	 * @param table_id
	 * @param user_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllWriteTableByTableId")
	public List<Object> getAllWriteTableByTableId(String table_id,String user_id){
		if(user_id == null || user_id.length() == 0)
			user_id = null;
		
		
		return inputService.getAllWriteTableByTableId(table_id, user_id);
	}
	
	@ResponseBody
	@RequestMapping("/getAllFile")
	public Map<String,Object> getAllFile(int page,int rows){
		// 1 获得指定的table_id的file总记录数
		int fileTotal = fileService.selectFileTotal();
		// 2 获得table集合
		List<File> fileList = fileService.getAllFile((page-1)*rows, rows);
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total", fileTotal);//---------------------------------这里要查询真实数据库中的总记录

		map.put("rows", fileList);
		return map;
	}

	@ResponseBody
	@RequestMapping("/validateFilePath")
	public Json validateFilePath(String input_id){
		Json json = new Json();
		if(input_id == null){
			json.setMsg("获取相关参数失败");
			json.setSuccess(false);
			return json;
		}
		
		boolean flag = fileService.validateFilePath(input_id);
		if(!flag){
			json.setMsg("文件下载失败");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("文件路径校验成功");
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/onloadFileByInputId")
	public void onloadFileByInputId(String input_id,String user_id,String type,HttpServletResponse response){
		if(input_id == null || user_id == null || type == null){
			System.out.println("获取参数失败");
		}
		
		
		//////////////////////////////////////
		logger.info("三个参数 input_id： "  + input_id + "user_id" +user_id+ "   type : " + type );
		//////////////////////////////////////
		
		
		
		
		boolean flag = fileService.onloadFileByInputId(input_id, user_id, type, response);
		System.out.println(flag);
		if(!flag){
			System.out.println("下载失败");
		}

	}
	
}















