package cn.edu.sspu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.User;
import cn.edu.sspu.pojo.Json;
import cn.edu.sspu.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService = null;
	
	/**
	 * 用户登录
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/login")
	public Json loginUser(String name,String password){
		
		Json json = new Json();
		if(name == null || password == null){
			json.setMsg("用户名 or 密码 请求参数为空");
			json.setSuccess(false);
			return json;
		}
		
		User user = null;
		try {
			user = userService.selectUserByNameAndPassword(name, password);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}catch (Exception e){
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		json.setSuccess(true);
		json.setObj(user);
		System.out.println(json);
		return json;
	}
	
	/**
	 * 获取所有的user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllUser")
	public Json getAllUser(){
		Json json = new Json();
		
		List<User> userList = null;
		try {
			userList = userService.getAllUser();
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}catch (Exception e){
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		
		json.setObj(userList);
		json.setSuccess(true);
		
		return json;
	}
	
	/**
	 * update单个user
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateUser")
	public Json updateUser(User user){
		Json json = new Json();
		if(user == null || user.getUser_id() == null){
			json.setMsg("获取参数封装user失败");
			json.setSuccess(false);
			return json;
		}
		int n;
		try {
			n = userService.updateUser(user);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		} catch(Exception e){
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		json.setObj(new Integer(n));
		json.setSuccess(true);

		return json;
	}

	/**
	 * 通过name删除user
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteUserByName")
	public Json deleteUserByName(String name){
		Json json = new Json();
		if(name == null){
			json.setMsg("获取name参数失败");
			json.setSuccess(false);
			return json;
		}
		
		int n;
		try {
			n = userService.deleteUserByName(name);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}catch (Exception e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		json.setObj(new Integer(n));
		json.setSuccess(true);
		
		return json;
	}
	
	/**
	 * 通过id删除user
	 * @param user_id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteUserById")
	public Json deleteUserById(String user_id){
		Json json = new Json();
		if(user_id == null){
			json.setMsg("获取user_id参数失败");
			json.setSuccess(false);
			return json;
		}
		
		int n;
		try {
			n = userService.deleteUserById(user_id);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}catch (Exception e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		json.setObj(new Integer(n));
		json.setSuccess(true);
		
		return json;
	}
	
	/**
	 * 插入user
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/insertUser")
	public Json insertUser(User user){
		Json json = new Json();
		
		//插入user 其id name password字段必须给出
		if(user == null || user.getUser_id() == null || user.getName() == null || user.getPassword() == null){
			json.setMsg("插入user的必要参数封装失败");
			json.setSuccess(false);
			return json;
		}
		
		int n = 0;
		try {
			n = userService.insertUser(user);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
		}
		json.setObj(new Integer(n));
		json.setSuccess(true);
		
		return json;
	}
	
	
}
