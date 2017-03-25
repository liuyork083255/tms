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

import cn.edu.sspu.dao.mapper.UserMapper;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.User;
import cn.edu.sspu.pojo.Json;
import cn.edu.sspu.service.UserService;
import cn.edu.sspu.utils.AdminUtils;
import cn.edu.sspu.utils.MailUtils;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService = null;
	
	@Autowired  
    private MailUtils mailUtil;
	
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
	@RequestMapping("/sendUserCheckCode")
	public Json sendUserCheckCode(String email,String username,HttpServletRequest request){
		Json json = new Json();
		if(email == null || username == null){
			json.setMsg("请输入用户名和email");
			json.setSuccess(false);
			return json;
		}
		
		//校验邮箱
		User dataUser = userService.selectUserByEmail(email);
		if(dataUser != null){
			json.setMsg("该邮箱已经被注册");
			json.setSuccess(false);
			return json;
		}
		
		String acticeCode = AdminUtils.getUUID().substring(0, 6);
		request.getSession().setAttribute(username, acticeCode);
		mailUtil.send(email, "系统验证码邮件","您的验证码是  " + acticeCode);
		json.setMsg("验证码已发送至您的邮箱，请及时完成校验，延期注册信息则作废");
		json.setSuccess(true);
		return json;
	}

	
	@ResponseBody
	@RequestMapping("/registerUser")
	public Json registerUser(@RequestBody User user,HttpServletRequest request,String checkCode){
		Json json = new Json();
		User dataUser = null;
		//校验用户名
		try {
			dataUser = userService.selectUserByName(user.getName());
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
		if(dataUser != null){
			json.setMsg("用户名重复");
			json.setSuccess(false);
			return json;
		}

		//校验验证码
		String sessionCheckCode = (String)request.getSession().getAttribute(user.getName());
		
		if(sessionCheckCode == null || !sessionCheckCode.equalsIgnoreCase(checkCode)){
			json.setMsg("验证码不正确");
			json.setSuccess(false);
			return json;
		}
		
		json = insertUser(user);
		
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
	
	@ResponseBody
	@RequestMapping("/sendForgetCheckCode")
	public Json sendForgetCheckCode(String email,HttpServletRequest request){
		Json json = new Json();
		
		//校验邮箱
		User dataUser = userService.selectUserByEmail(email);
		if(dataUser == null){
			json.setMsg("不存在该邮箱");
			json.setSuccess(false);
			return json;
		}
		
		
		String acticeCode = AdminUtils.getUUID().substring(0, 8);
		
		request.getSession().setAttribute(email, acticeCode);
		
		mailUtil.send(email, "系统验证码邮件","您的验证码是  " + acticeCode);
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/checkForgetCheckCode")
	public Json checkForgetCheckCode(String email,String checkCode,HttpServletRequest request){
		Json json = new Json();
		
		String sessionCheckCode = (String)request.getSession().getAttribute(email);
		
		if(sessionCheckCode == null || !sessionCheckCode.equalsIgnoreCase(checkCode)){
			json.setMsg("验证码不正确");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("验证码成功,请修改密码");
		json.setSuccess(true);
		return json;
	}

	@ResponseBody
	@RequestMapping("/updateUserPassword")
	public Json updateUserPassword(String password,String email){
		Json json = new Json();
		
		if(password == null || email == null){
			json.setMsg("获取email 和 password 失败");
			json.setSuccess(false);
			return json;
		}
		
		User user = null;
		user = userService.selectUserByEmail(email);
		if(user == null){
			json.setMsg("获取指定的邮箱的用户失败");
			json.setSuccess(false);
			return json;
		}
		
		user.setPassword(password);
		int n = 0;
		try {
			n = userService.updateUser(user);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		
		if(n == 0){
			json.setMsg("更改出现未知异常");
			json.setSuccess(false);
			return json;
		}
		
		json.setMsg("更改密码成功");
		json.setSuccess(true);
		return json;
	}
	@ResponseBody
	@RequestMapping("/userSendEmailToAdmin")
	public Json  userSendEmailToAdmin(HttpServletRequest request,String text){
		Json json = new Json();
		User sessionUser = (User) request.getSession().getAttribute("user");
		if(sessionUser == null){
			json.setMsg("获取session中user失败，你的登陆可能已经过期");
			json.setSuccess(false);
			return json;
		}
		
		String adminEmail = AdminUtils.getAdminEmail("adminEmail");
		
		try{
			mailUtil.send(adminEmail, "来自"+sessionUser.getName()+"用户的邮件",text);
		}catch(Exception e){
			e.printStackTrace();
			json.setMsg("邮件发送失败");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("发送成功");
		json.setSuccess(true);
		return json;
	}
	
}


























