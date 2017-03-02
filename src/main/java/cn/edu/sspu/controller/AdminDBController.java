package cn.edu.sspu.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.mysqlx.protobuf.MysqlxDatatypes.Array;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.pojo.Json;
import cn.edu.sspu.service.TableService;
import cn.edu.sspu.utils.AdminUtils;



@Controller
@RequestMapping("/admin/db")
public class AdminDBController {
	@Autowired
	private TableService tableService = null;
	
	//测试Model
	private Model model;
	
	/*该方法是接收一个tableName参数，判断数据库是否存在，存在则Json返回false，否则返回true*/
	@ResponseBody
	@RequestMapping("/selecttablebyname")
	public Json selectTableByName(String name){
		Json json = new Json();
		
		if(name == null){
			json.setMsg("获取name参数失败");
			json.setSuccess(false);
			return json;
		}
		
		Table table = null;
		try {
			table = tableService.selectTableByName(name);
		}catch (Exception e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		
		if(table != null){
			json.setMsg("改模板名称已存在");
			json.setSuccess(false);
			return json;
		}
		json.setSuccess(true);
		return json;
	}

	/*该方法接收一个model对象，存入数据库中，成功Json返回true，否则返回false*/
	@ResponseBody
	@RequestMapping("/savemodel")
	public Json saveModel(@RequestBody Model model){
		Json json = new Json();
		
		if(model == null){
			json.setMsg("获取model参数失败  ");
			json.setSuccess(false);
			return json;
		}
		
		Model newModel = AdminUtils.setModelIdAndInputId(model);
		
		try {
			boolean flag = tableService.insertModel(newModel);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		json.setSuccess(true);
		json.setMsg("上传成功  ");
		
		return json;
	}

	
	/*该方法是获取所有的table并以list集合方式返回，并且会有page和rows两个参数用于分页*/
	@ResponseBody
	@RequestMapping("/getAllTableByPage")
	public Map<String,Object> getAllTableByPage(int page,int rows){
		// 1 获得table总记录数
		int tableTotal = tableService.selectTableTotal();
		// 2 获得table集合
		List<Table> tableList = tableService.selectTableByPage((page-1)*rows, rows);
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total", tableTotal);//---------------------------------这里要查询真实数据库中的总记录

		map.put("rows", tableList);
		return map;
	}
	
	/*该方法参数是一个table对象，保存在数据库中*/
	@ResponseBody
	@RequestMapping("/savetable")
	public Json saveTable(@RequestBody Table table){
		Json json = new Json();
		System.out.println(JSON.toJSONString(table, true));
		return json;
	}
	
	/*该方法接收一个table_id参数，返回model对象*/
	@ResponseBody
	@RequestMapping("/getModelReturnJson")
	public Json getModelReturnJson(String table_id){
		Json json = new Json();
		
		
		
		json.setSuccess(true);
		return json;
	}
	
	/*该方法参数是table_id，返回inputList模板，之所以不返回model，因为前段用dadagrid显示，要求是list类型*/
	@ResponseBody
	@RequestMapping("/getModelReturnInputList")
	public List<Input> getModelReturnInputList(String table_id){
		/*System.out.println(table_id);
		if(this.model != null)
			return this.model.getInputList();*/
		
		
		
		
		
		
		
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
	
	
	
	
	@RequestMapping("/testFun")
	public void testFun(int num1,int num2){
		List<Table> n = tableService.selectTableByPage(num1,num2);
		System.out.println(JSON.toJSONString(n, true));
	}
	
	
}
