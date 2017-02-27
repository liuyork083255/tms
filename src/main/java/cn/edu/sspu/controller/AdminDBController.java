package cn.edu.sspu.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.mysqlx.protobuf.MysqlxDatatypes.Array;

import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Form;
import cn.edu.sspu.pojo.Json;
import cn.edu.sspu.utils.AdminUtils;



@Controller
@RequestMapping("/admin/db")
public class AdminDBController {
	
	//测试Model
	private Model model;
	
	/*该方法是接收一个tableName参数，判断数据库是否存在，存在则Json返回false，否则返回true*/
	@ResponseBody
	@RequestMapping("/selecttablebyname")
	public Json selectTableByName(String name){
		System.out.println(name);
		Json json = new Json();
		json.setMsg("");
		json.setSuccess(true);
		return json;
	}

	/*该方法接收一个model对象，存入数据库中，成功Json返回true，否则返回false*/
	@ResponseBody
	@RequestMapping("/savemodel")
	public Json saveModel(@RequestBody Model model){
		Json json = new Json();
		System.out.println("转换前：");
		System.out.println(JSON.toJSONString(model, true));
		
		this.model = AdminUtils.setModelIdAndInputId(model);
		System.out.println("转换前：");
		System.out.println(JSON.toJSONString(this.model, true));
		
		return json;
	}

	
	/*该方法是获取所有的table并以list集合方式返回，并且会有page和rows两个参数用于分页*/
	@ResponseBody
	@RequestMapping("/getalltables")
	public Map<String,Object> getAllTables(int page,int rows){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", 100);//---------------------------------这里要查询真实数据库中的总记录
		List<Form> tablelist = new ArrayList<Form>();
		for(int i=0;i<rows;i++){
			Form table = new Form();
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
	public Json saveTable(@RequestBody Form table){
		Json json = new Json();
		System.out.println(JSON.toJSONString(table, true));
		return json;
	}
	
	/*该方法接收一个table_id参数，返回model对象*/
	@ResponseBody
	@RequestMapping("/getmodelbytableid")
	public Model getModelByTableId(String table_id){
		System.out.println("请求参数：" + table_id);
		return this.model;
	}
	
	/*该方法参数是table_id，返回inputList模板，之所以不返回model，因为前段用dadagrid显示，要求是list类型*/
	@ResponseBody
	@RequestMapping("/getinputlist")
	public List<Input> getModel(String table_id){
		System.out.println(table_id);
		if(this.model != null)
			return this.model.getInputList();
		return null;
	}
	
	/*该方法是接收一个input对象，然后保存入库，这个对象必须有table_id，不然无法保存*/
	@ResponseBody
	@RequestMapping("/saveinput")
	public Json saveInput(@RequestBody Input input){
		Json json = new Json();
		System.out.println(JSON.toJSONString(AdminUtils.setInputId(input), true));
		return json;
	}
	
	/*该方法是接收一个input的id参数集合，然后转为list集合，将里面的所有input_id全部删除*/
	@ResponseBody
	@RequestMapping("/deleteinput")
	public Json deleteInput(String input_ids){
		Json json = new Json();
		
		String[] inputString = input_ids.split("-");
		
		List<String> inputList = Arrays.asList(inputString);
		
		for(String s : inputList)
			System.out.println(s);
		json.setSuccess(true);
		return json;
	}
	
	
	
	
}
