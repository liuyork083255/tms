package cn.edu.sspu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;
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

	
	/*该方法是获取所有的table并以list集合方式返回，并且会有page和rows两个参数用于分页*/
	@ResponseBody
	@RequestMapping("/getalltables")
	public Map<String,Object> getAllTables(int page,int rows){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", 100);//---------------------------------这里要查询真实数据库中的总记录
		List<Table> tablelist = new ArrayList<Table>();
		for(int i=0;i<rows;i++){
			Table table = new Table();
			table.setTable_id("0000" + i);
			table.setName("name" + i);
			table.setCreatetime("2016-11-13 19:34:45");
			table.setInfo("垃圾信息"+i);
			tablelist.add(table);
		}
		map.put("rows", tablelist);
		return map;
	}
	
	/*该方法参数是一个table对象，保存在数据库中*/
	@ResponseBody
	@RequestMapping("/savetable")
	public Json saveTable(@RequestBody Table table){
		Json json  = new Json();
		if(table != null){
			System.out.println(table);
			json.setSuccess(true);
		}else{
			json.setSuccess(false);
		}
		return json;
	}
	
	
	
	
	
}
