package cn.edu.sspu.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import sun.awt.RequestFocusController;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.User;
import cn.edu.sspu.pojo.Json;
import cn.edu.sspu.service.UserTableService;
import cn.edu.sspu.utils.AdminUtils;

@Controller
@RequestMapping("/userTable")
public class UserTableController {
	@Autowired
	private UserTableService userTableService;

	/**
	 * 这个方法是查询一个指定用户还没有填写的所有表单
	 * 这个user_id参数是从session中获取，而不是直接传进来的
	 * 这个方式返回值是map类型，采用了分页
	 * @param request
	 * @return
	 * @throws ServiceException 
	 */
	@ResponseBody
	@RequestMapping("/selectTableUnwriteByUserId")
	public Map<String,Object> selectTableUnwriteByUserId(HttpServletRequest request,int page,int rows) throws ServiceException{
		/*获取总数*/
		int total = userTableService.selectTableUnwriteTotal();
		List<Table> tableList = null;
		User sessionUser = (User)request.getSession().getAttribute("user");
		if(sessionUser != null){
			tableList = userTableService.selectTableUnwriteByUserId(sessionUser.getUser_id(),(page-1)*rows, rows);
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total", total);//---------------------------------这里要查询真实数据库中的总记录

		map.put("rows", tableList);
		return map;

	}
	
	@ResponseBody
	@RequestMapping("/savaFileToSession")
	public Json savaFileToSession(HttpServletResponse response,HttpServletRequest request,
			String input_id,@RequestParam(value="file", required=false) MultipartFile file) throws Exception{
		Json json = new Json();
		if(input_id == null){
			request.setAttribute("msg", "获取input_id参数失败");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		if(file == null || file.getOriginalFilename() == null || file.getOriginalFilename() == ""){
			request.setAttribute("msg", "文件名不能为空");
			request.getRequestDispatcher("/showMessage.jsp").forward(request, response);
			return null;
		}
		
		cn.edu.sspu.models.File sessionFile = new cn.edu.sspu.models.File();
		
		sessionFile.setFilename(file.getOriginalFilename());
		sessionFile.setFiletype(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
		sessionFile.setUploadtime(AdminUtils.getCurrentTime());
		
		request.getSession().setAttribute(input_id, sessionFile);
		
		json.setMsg("上传成功");
		json.setSuccess(true);
		return json;
	}
	
	
	
	
	@ResponseBody
	@RequestMapping("/fileUpTest")
	public Json fileUpTest(HttpServletResponse response,HttpServletRequest request,String input_id,
            @RequestParam(value="file", required=true) MultipartFile file) throws IOException{
		Json json = new Json();
		
		if(file == null || file.getOriginalFilename() == null || file.getOriginalFilename() == ""){
			json.setMsg("上传的文件为空");
			json.setSuccess(false);
			return json;
		}
		
		
		String filePath = "F:\\develop\\fileup\\";

		String newFileName = input_id + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		
		File fileIO = new File(filePath + newFileName);
		
		try {
			file.transferTo(fileIO);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("文件写入内存失败");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("上传成功");
		json.setSuccess(true);
		return json;
	}
	
}
















