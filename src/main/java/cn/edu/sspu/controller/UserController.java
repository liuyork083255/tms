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
	
}
