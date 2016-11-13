package cn.edu.sspu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.models.Model;
import cn.edu.sspu.pojo.Json;



@Controller
@RequestMapping("/admin/db")
public class AdminDBController {
	
	
	
	/*该方法是接收一个tableName参数，判断数据库是否存在，存在则Json返回false，否则返回true*/
	@ResponseBody
	@RequestMapping("/selectTableByName")
	public Json selectTableByName(String name){
		System.out.println("selectTableByName() 访问成功");
		
		Json json = new Json();
		if(name !=null && name.equals("table")){
			json.setSuccess(true);
			json.setMsg("tableName验证成功！");
		}else{
			json.setSuccess(false);
			json.setMsg("tableName验证失败！");
		}
		System.out.println(json);
		return json;
	}

	/*该方法接收一个model对象，存入数据库中，成功Json返回true，否则返回false*/
	@ResponseBody
	@RequestMapping("/savemodel")
	public Json saveModel(@RequestBody Model model){
		Json json = new Json();
		if (model != null) {
			System.out.println(JSON.toJSONString(model, true));
			json.setSuccess(true);
			json.setMsg("model保存成功");
		}else{
			json.setSuccess(false);
			json.setMsg("model保存失败");
		}
		return json;
	}

	
	
	
	
	
	
	
	
	
	
}
