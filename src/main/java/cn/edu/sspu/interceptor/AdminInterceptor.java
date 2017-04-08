package cn.edu.sspu.interceptor;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.models.User;
import cn.edu.sspu.pojo.Json;


public class AdminInterceptor extends HandlerInterceptorAdapter {
	//定义不拦截的URL
	final private static String LOGIN = "/admin/db/login.do";

	/**
	 * 请求前进行拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		String requestUri = request.getRequestURI();  //requestUri : /tms/admin/db/selecttablebyname.do
        String contextPath = request.getContextPath();  //contextPath : /tms
        String url = requestUri.substring(contextPath.length()); //url : /admin/db/selecttablebyname.do
        ArrayList<String> list = new ArrayList<String>();
        list.add(LOGIN);
        //这里要注意：不能拦截用户的登陆，注册请求，验证码...等等请求，有点复杂，需要一个个去删除，可以看出这里没有设计好
        if(list.contains(url)){
        	System.out.println("这个url不进行拦截");
        	return true;
        }else{
        	//别的所有拦截进行拦截
    		Object obj = request.getSession().getAttribute("user");
    		if(obj == null){
    			if("GET".equalsIgnoreCase(request.getMethod())){
    				System.out.println("get 拦截");
    				response.sendRedirect("/tms/html/admin/index.html");
    				return false;
    			}else{
    				System.out.println("post 拦截");
    	        	Json json = new Json();
    	        	json.setMsg("你的登陆已过期，请重新登陆");
    	        	json.setSuccess(false);
    	        	response.getWriter().print(JSON.toJSONString(json));
    	        	return false;
    			}
    		}
			try{
				User user = (User)obj;
			}catch(Exception e){
    			if("GET".equalsIgnoreCase(request.getMethod())){
    				response.sendRedirect("/tms/html/admin/index.html");
    				return false;
    			}else{
    	        	Json json = new Json();
    	        	json.setMsg("你的登陆已过期，请重新登陆");
    	        	json.setSuccess(false);
    	        	response.getWriter().print(JSON.toJSONString(json));
    	        	return false;
    			}
			}
			return true;
        }
		
	}
	
	/**
	 * 后端响应完成后执行
	 */
	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
	}
}
