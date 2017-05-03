package cn.edu.sspu.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.pojo.Json;

public class GlobalException implements HandlerExceptionResolver {
	private static Logger logger = LoggerFactory.getLogger(GlobalException.class);
	
	public ModelAndView resolveException(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex) {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		System.out.println(ex.getMessage());
		Json json = new Json();
		json.setMsg("发生异常，被全局异常捕获 : " + ex.getMessage());
		json.setSuccess(false);
		try {
			response.getWriter().print(JSON.toJSONString(json));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	    return new ModelAndView();
	}

}
