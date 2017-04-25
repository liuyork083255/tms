package cn.edu.sspu.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import sun.awt.RequestFocusController;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.InputName_Value;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.TableIdAndName;
import cn.edu.sspu.models.User;
import cn.edu.sspu.pojo.Json;
import cn.edu.sspu.service.FileService;
import cn.edu.sspu.service.InputService;
import cn.edu.sspu.service.TableService;
import cn.edu.sspu.service.UserTableService;
import cn.edu.sspu.utils.AdminUtils;
import cn.edu.sspu.utils.RedisUtils;
import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("/userTable")
public class UserTableController {
	@Autowired
	private UserTableService userTableService;
	@Autowired
	private TableService tableService;
	@Autowired
	private InputService inputService;
	@Autowired
	private FileService fileService;
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	/**
	 * 这个方法是查询一个指定用户还没有填写的所有表单
	 * 这个user_id参数是从session中获取，而不是直接传进来的
	 * 这个方式返回值是map类型，采用了分页
	 * @param request
	 * @return
	 * @throws ServiceException 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@ResponseBody
	@RequestMapping("/selectTableUnwriteByUserId")
	public Map<String,Object> selectTableUnwriteByUserId(HttpServletRequest request,HttpServletResponse response, int page,int rows) throws Exception{
		List<Table> tableList = null;
		User sessionUser = (User)request.getSession().getAttribute("user");
		if(sessionUser == null){
			request.setAttribute("msg", "获取session中的User失败，请重新登录");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		tableList = userTableService.selectTableUnwriteByUserId(sessionUser.getUser_id(),(page-1)*rows, rows);
		/*获取总数*/
		int total = userTableService.selectTableUnwriteTotal(sessionUser.getUser_id());
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total", total);//---------------------------------这里要查询真实数据库中的总记录

		map.put("rows", tableList);
		return map;

	}
	
	/**
	 * 该方法是根据一个指定的用户查询已经填写的table
	 * 这个user_id参数是从session中获取，而不是直接传进来的
	 * 这个方式返回值是map类型，采用了分页
	 * @param request
	 * @param page
	 * @param rows
	 * @return
	 * @throws ServiceException
	 */
	@ResponseBody
	@RequestMapping("/selectTableWriteByUserId")
	public Map<String,Object> selectTableWriteByUserId(HttpServletRequest request,HttpServletResponse response, int page,int rows) throws Exception{
		List<Table> tableList = null;
		User sessionUser = (User)request.getSession().getAttribute("user");
		if(sessionUser == null){
			request.setAttribute("msg", "获取session中的User失败，请重新登录");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		tableList = userTableService.selectTableWriteByUserId(sessionUser.getUser_id(),(page-1)*rows, rows);
		
		/*获取总数*/
		int total = userTableService.selectTableWriteTotal(sessionUser.getUser_id());
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total", total);//---------------------------------这里要查询真实数据库中的总记录

		map.put("rows", tableList);
		return map;
	}
	
	/**
	 * 这个方法将上传的文件存入硬盘，同时将路径保存在session中，如果保存
	 * model报错，那么就要根据这个路径将文件删除
	 * @param response
	 * @param request
	 * @param input_id
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/savaFileToSession")
	public Json savaFileToSession(HttpServletResponse response,HttpServletRequest request,
			String input_id,@RequestParam(value="file", required=false) MultipartFile file) throws Exception{
		Json json = new Json();
		if(input_id == null){
			request.setAttribute("msg", "获取input_id参数失败");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		if(file == null || file.getOriginalFilename() == null || file.getOriginalFilename() == ""){
			request.setAttribute("msg", "文件名不能为空");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		User user = (User)request.getSession().getAttribute("user");
		if(user == null){
			request.setAttribute("msg", "Session中获取当前用户失败，可能是登陆已过期");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		
		String newFileName = input_id + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		
		
		cn.edu.sspu.models.File sessionFile = new cn.edu.sspu.models.File();  
		sessionFile.setInput_id(input_id);
		sessionFile.setUser_id(user.getUser_id());
		sessionFile.setFilename(file.getOriginalFilename());
		sessionFile.setUploadtime(AdminUtils.getCurrentTime());
		sessionFile.setFiletype(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
		
		request.getSession().setAttribute(input_id, sessionFile);
		
		//将文件存入redis中，以inputid作为key
		try{
		RedisUtils.getRedisMaster();
		RedisUtils.setValue(input_id, file.getBytes());
		}catch(Exception e){
			logger.error("文件上传存入redis操作失败，异常信息为 ： " + e.getMessage());
			request.setAttribute("msg", "文件上传存入redis操作失败");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		
/*		String filePath = AdminUtils.getFileUploadPath("fileUploadPath");
		if(filePath == null){
			request.setAttribute("msg", "获取配置文件上传路径失败");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		
		File fileIO = new File(filePath + user.getUser_id() + "\\" + newFileName);
		
		if(!fileIO.exists()){
			boolean mkdirFlag = fileIO.mkdirs();
			if(!mkdirFlag)
				throw new ServiceException("创建文件路径失败");
		}
		
		try {
			file.transferTo(fileIO);
		} catch (Exception e) {
			logger.error(e.getMessage());
			json.setMsg("文件写入内存失败");
			json.setSuccess(false);
			return json;
		}*/
		json.setMsg("上传成功");
		json.setSuccess(true);
		return json;

	}
	
	@ResponseBody
	@RequestMapping("/saveUserEditModel")
	public Json saveUserEditModel(@RequestBody Model model,HttpServletRequest request){
		Json json = new Json();
		//判断session中是否有user
		User sessionUser = null;
		try{
			sessionUser = (User)request.getSession().getAttribute("user");
			if(sessionUser == null){
				json.setMsg("session中没有当前用户，请重新登录");
				json.setSuccess(false);
				return json;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			json.setMsg("session中没有当前用户，请重新登录");
			json.setSuccess(false);
			return json;
		}
		// 判断model中的必要参数
		if(model == null || model.getInputList() == null || model.getTable_id() == null){
			json.setMsg("保存数据model中参数封装失败");
			json.setSuccess(false);
			return json;
		}
		
		//设置model的user_id
		model.setUser_id(sessionUser.getUser_id());
		//设置所有input的user_id
		model.setInputList(AdminUtils.setAllnputUserId(model.getInputList(), model.getUser_id()));
		
		// 数据到数据库，file交给service层来处理，如果file写入硬盘报错，那么就回滚事务
		boolean flag;
		try {
			//这一步很关键，新增的功能，就是一张表能够再次填写
			int max = inputService.selectInputTimesMax(model.getTable_id(), sessionUser.getUser_id());
			AdminUtils.setInputTimes(model.getInputList(),max+1);
			flag = userTableService.saveUserEditModel(model, request.getSession());
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		if(!flag){
			json.setMsg("插入失败，异常未知");
			json.setSuccess(false);
			return json;
		}
		
		
		json.setMsg("保存成功！！！");
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * 该方法是获取指定user填写了的input集合，返回以model集合返回
	 * @param request
	 * @param table_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getModel_WritedInput")
	public Json getModel_WritedInput(HttpServletRequest request,String table_id,String times){
		Json json = new Json();
		User sessionUser = (User)request.getSession().getAttribute("user");
		if(sessionUser == null || table_id == null || times == null){
			json.setMsg("获取session中User 或者 封装table_id参数失败");
			json.setSuccess(false);
			return json;
		}
		Model model = new Model();
		//封装model数据
		model.setTable_id(table_id);
		model.setUser_id(sessionUser.getUser_id());
		Table table = tableService.selectTableById(table_id);
		if(table == null){
			json.setMsg("根据table_id查询table失败");
			json.setSuccess(false);
			return json;
		}
		model.setName(table.getName());
		List<Input> inputList = null;
		try {
			inputList= inputService.selectInputByUserIdAndTableIdAndTimes(table_id, sessionUser.getUser_id(), Integer.parseInt(times));
		} catch (Exception e) {
			logger.error(e.getMessage());
			json.setMsg("根据table_id和user_id查询input集合失败");
			json.setSuccess(false);
			return json;
		}
		model.setInputList(inputList);
		
		json.setObj(model);
		json.setSuccess(true);
		return json;
	}
	
	
	/**
	 * 这个方法是用于用户修改表单字段，并且是以input为单位进行修改，而不是整个inputList进行修改
	 * @param input_id
	 * @param input_value
	 * @param type
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateInputValueById")
	public Json updateInputValueById(String input_id,String input_value,String type,HttpServletRequest request){
		Json json = new Json();
		if(input_id == null || type == null){
			json.setMsg("获取input_id、type 参数失败");
			json.setSuccess(false);
			return json;
		}
		boolean flag = false;
		if(type.equals("file")){
			cn.edu.sspu.models.File sessionFile = (cn.edu.sspu.models.File) request.getSession().getAttribute(input_id);
			if(sessionFile == null){
				json.setMsg("获取session中的file参数失败");
				json.setSuccess(false);
				return json;
			}
			input_value = sessionFile.getFilename();
			try {
				flag = userTableService.updataInputValueById(input_id, input_value, type, sessionFile);
			} catch (Exception e) {
				logger.error(e.getMessage());
				json.setMsg("update 发生未知错误，请联系管理员");
				json.setSuccess(false);
				return json;
			}
			
			
		}else{
			try {
				flag = userTableService.updataInputValueById(input_id, input_value, type, null);
			}
			catch (Exception e) {
				logger.error(e.getMessage());
				json.setMsg("update 发生未知错误，请联系管理员");
				json.setSuccess(false);
				return json;
			}
		}
		if(!flag){
			json.setMsg("update 失败，请联系管理员");
			json.setSuccess(false);
			return json;
		}
		
		json.setMsg("修改成功 ");
		json.setSuccess(true);
		return json;
	}
	
	
	@ResponseBody
	@RequestMapping("/deleteUserWriteTable")
	public Json deleteUserWriteTable(HttpServletRequest request,String table_id,String times){
		Json json  = new Json();
		User user = (User) request.getSession().getAttribute("user");
		if(table_id == null || user == null){
			json.setMsg("获取table_id 获取获取 session中的user 失败");
			json.setSuccess(false);
			return json;
		}
		
		boolean flag = false;
		try {
			flag = userTableService.deleteUserWriteTable(table_id, user.getUser_id(),times);
		} catch (Exception e) {
			logger.error(e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		
		if(!flag){
			json.setMsg("删除出现未知异常");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("删除成功");
		json.setSuccess(true);
		return json;
		
	}
	
	@ResponseBody
	@RequestMapping("/getTableById")
	public Json getTableById(String table_id){
		Json json  = new Json();
		
		if(table_id == null){
			json.setMsg("获取tableid失败");
			json.setSuccess(false);
			return json;
		}
		
		List<Input> table = null;
		try {
			table = inputService.selectInputByTableId(table_id);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg("获取table模板失败");
			json.setSuccess(false);
			return json;
		}
		json.setObj(table);
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * 这个方法获取的是一个用户填一张表的所有记录，可能是一次，可能是多次
	 * @param table_id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getTableIdAndNameByTableIdAndUserId")
	public List<Object> getTableIdAndNameByTableIdAndUserId(String table_id,HttpServletRequest request,HttpServletResponse response) throws Exception{
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			request.setAttribute("msg", "Session中获取当前用户失败，可能是登陆已过期");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		if(table_id == null){
			request.setAttribute("msg", "table_id参数获取失败");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		
		List<Object> objectList = new ArrayList<Object>();
		objectList = inputService.selectAllInputByTimes(table_id,user.getUser_id());
		
		
		return objectList;
	}
	
	
	@ResponseBody
	@RequestMapping("/selectHistoryByUserId_InputName")
	public List<Object> selectHistoryByUserId_InputName(HttpServletRequest request,String inputname){
		User user = (User)request.getSession().getAttribute("user");
		if(user == null)
			return null;
		return inputService.selectHistoryByUserId_InputName(user.getUser_id(), inputname);
	}

	
	
	@ResponseBody
	@RequestMapping("/getUserAllFile")
	public Map<String,Object> getUserAllFile(int page,int rows,HttpServletRequest request){
		User user = null;
		try{
			user = (User)request.getSession().getAttribute("user");
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		// 1 获得指定的table_id的file总记录数
		int fileTotal = fileService.selectFileTotalByUserId(user.getUser_id());
		// 2 获得table集合
		List<cn.edu.sspu.models.File> fileList = fileService.getAllFileByUserId((page-1)*rows, rows,user.getUser_id());
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total", fileTotal);//---------------------------------这里要查询真实数据库中的总记录

		map.put("rows", fileList);
		return map;
	}
	
	
	@ResponseBody
	@RequestMapping("/fileUpTest")
	public Json fileUpTest( String name){
		System.out.println(name);
		return null;
	}
	
}
















