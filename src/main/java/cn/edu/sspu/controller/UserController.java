package cn.edu.sspu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.User;
import cn.edu.sspu.pojo.Json;
import cn.edu.sspu.service.UserService;
import cn.edu.sspu.utils.AdminUtils;

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
	public Json loginUser(String name,String password,HttpServletRequest request){
		
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
		//将user存入session中
		request.getSession().setAttribute("user", user);
		return json;
	}

	
	@ResponseBody
	@RequestMapping("/registerUser")
	public Json registerUser(@RequestBody User user,HttpServletRequest request){
		Json json = new Json();
		
		json = insertUser(user);
		
		System.out.println(JSON.toJSONString(json, true));
		if(json.isSuccess())
			request.getSession().setAttribute("user", (User)json.getObj());
		
		return json;
	}
	

	/**
	 * 通过session获取user
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUserThroughSession")
	public Json getUserThroughSession(HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		Json json = new Json();
		
		if(user == null){
			json.setMsg("session中获取user失败");
			json.setSuccess(false);
			return json;
		}
		json.setObj(user);
		json.setSuccess(true);
		return json;
	}
	
	
	/**
	 * 通过name查找user
	 * @param name
	 * @return
	 */
	@ ResponseBody
	@RequestMapping("/selectUserByName")
	public Json selectUserByName(String name){
		Json json = new Json();
		
		if(name == null){
			json.setMsg("获取name参数失败");
			json.setSuccess(false);
			return json;
		}
		User user = null;
		try {
			user = userService.selectUserByName(name);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		} catch (Exception e){
			json.setMsg("未知的查询异常");
			json.setSuccess(false);
			e.printStackTrace();
			return json;
		}
		json.setObj(user);
		json.setSuccess(true);
		return json;

	}
	
	

	/**
	 * 通过id查找user
	 * @param user_id
	 * @return
	 */
	@ ResponseBody
	@RequestMapping("/selectUserById")
	public Json selectUserById(String user_id){
		Json json = new Json();
		
		if(user_id == null){
			json.setMsg("获取user_id参数失败");
			json.setSuccess(false);
			return json;
		}
		User user = null;
		try {
			user = userService.selectUserById(user_id);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		} catch (Exception e){
			json.setMsg("未知的查询异常");
			json.setSuccess(false);
			e.printStackTrace();
			return json;
		}
		json.setObj(user);
		json.setSuccess(true);
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
	public Json updateUser(@RequestBody User user,HttpServletRequest request){
		Json json = new Json();
		if(user == null){
			
			json.setMsg("获取参数封装user失败");
			json.setSuccess(false);
			return json;
		}
		
		if(user.getUser_id() == null){
			if(request.getSession().getAttribute("user") == null){
				json.setMsg("修改的user在session和参数中都没有找到user_id，无法修改 ！！！");
				json.setSuccess(false);
				return json;
			}else
				user.setUser_id(((User)request.getSession().getAttribute("user")).getUser_id());
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
		request.getSession().setAttribute("user", user);
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
		if(user == null || user.getName() == null || user.getPassword() == null){
			json.setMsg("插入user的必要参数封装失败");
			json.setSuccess(false);
			return json;
		}
		
		if(user.getUser_id() == null)
			user.setUser_id(AdminUtils.getUUID());;
		
		int n = 0;
		try {
			n = userService.insertUser(user);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
		}
		json.setObj(user);
		json.setSuccess(true);
		
		return json;
	}
	
	public User getSessionUser(HttpServletRequest request){
		return (User)request.getSession().getAttribute("user");
	}
	
	
}
