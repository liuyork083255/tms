package cn.edu.sspu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.sspu.models.Model;

@Controller
@RequestMapping("/admin/create")
public class AdminCreateModelController {

	@RequestMapping("/test1")
	public void test1(@RequestBody Model model, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("zhuan fa  success");
		request.getRequestDispatcher("/admin/db/savemodel.do").forward(request, response);
	}

}
