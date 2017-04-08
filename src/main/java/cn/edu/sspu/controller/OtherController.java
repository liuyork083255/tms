package cn.edu.sspu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.models.User;
import cn.edu.sspu.pojo.Json;

@Controller
@RequestMapping("/other")
public class OtherController {
	@RequestMapping("/loginout")
	public void fun1(HttpServletRequest request,HttpServletResponse response,String type) throws Exception{
		request.getSession().removeAttribute("user");
		if(type.equalsIgnoreCase("0")){
			response.sendRedirect("/tms/html/admin/index.html");
		}else{
			response.sendRedirect("/tms/html/user/index.html");
		}
    	
	}
}
