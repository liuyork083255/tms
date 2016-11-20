package cn.edu.sspu.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.pojo.User;

@Controller
@RequestMapping("/admin/create")
public class AdminCreateModelController {

	@RequestMapping("/test1")
	public void test1(String userName){
		System.out.println(userName);
	}

}
