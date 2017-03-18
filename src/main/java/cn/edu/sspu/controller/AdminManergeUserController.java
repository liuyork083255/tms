package cn.edu.sspu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.EmailModel;
import cn.edu.sspu.models.User;
import cn.edu.sspu.pojo.Json;
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
	
}















