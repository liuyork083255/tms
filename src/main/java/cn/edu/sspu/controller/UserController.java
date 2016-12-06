package cn.edu.sspu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.sspu.models.User;
import cn.edu.sspu.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService = null;
	
	@RequestMapping("/login")
	public void loginUser(String name){
		System.out.println("controller is ok");
		User user = userService.selectUserByName(name);
		System.out.println(user);
	}

}
