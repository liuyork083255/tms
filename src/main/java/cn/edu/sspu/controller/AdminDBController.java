package cn.edu.sspu.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.AdminGodownExcelSession;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.TableIdAndName;
import cn.edu.sspu.models.User;
import cn.edu.sspu.models.User_Table;
import cn.edu.sspu.models.searchfilter.UserTableSearch;
import cn.edu.sspu.pojo.Json;
import cn.edu.sspu.service.ExportToExcelService;
import cn.edu.sspu.service.InputService;
import cn.edu.sspu.service.TableService;
import cn.edu.sspu.service.UserService;
import cn.edu.sspu.service.UserTableService;
import cn.edu.sspu.utils.AdminUtils;



@Controller
@RequestMapping("/admin/db")
public class AdminDBController {
	@Autowired
	private TableService tableService = null;
	@Autowired
	private UserService userService = null;
	@Autowired
	private InputService inputService;
	@Autowired
	private UserTableService userTableService;
	@Autowired
	private ExportToExcelService exportToExcelService;
	
	private static Logger logger = LoggerFactory.getLogger(AdminDBController.class);
	
	@ResponseBody
	@RequestMapping("/login")
	public Json login(String name,String password,HttpServletRequest request){
		Json json = new Json();
		if(name == null || password == null){
			json.setMsg("用户名 or 密码 请求参数为空");
			json.setSuccess(false);
			return json;
		}
		
		User user = null;
		try {
			user = userService.selectUserByNameAndPassword(name, password);
		} catch (ServiceException e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}catch (Exception e){
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		if(!user.getType().equalsIgnoreCase("0")){
			json.setMsg("你不是管理员身份");
			json.setSuccess(false);
			return json;
		}
		
		json.setSuccess(true);
		json.setObj(user);
		//将user存入session中
		request.getSession().setAttribute("user", user);
		logger.info("用户名 ：" + user.getName() +" id : " + user.getUser_id() + " 登陆");
		return json;
	}
	
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
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
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
		
		if(map.size() == 0)
			logger.error("/getAllTableByPage.do 方法获取返回为空");
		
		map.put("total", tableTotal);//---------------------------------这里要查询真实数据库中的总记录

		map.put("rows", tableList);
		return map;
	}
	
	/*该方法参数是一个table对象，保存在数据库中*/
	@ResponseBody
	@RequestMapping("/updateTable")
	public Json updateTable(@RequestBody Table table){
		Json json = new Json();
		if(table.getTable_id() == null){
			json.setMsg("table参数中没有包含table_id");
			json.setSuccess(false);
			return json;
		}
		
		
		try {
			tableService.updateTable(table);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}catch (Exception e) {
			logger.error(e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		
		json.setSuccess(true);
		return json;
	}
	
	/*该方法接收一个table_id参数，返回model对象*/
	@ResponseBody
	@RequestMapping("/getModelReturnJson")
	public Json getModelReturnJson(String table_id,String param,HttpServletRequest request){
		Json json = new Json();
		
		if(table_id == null){
			json.setMsg("获取table_id失败 ");
			json.setSuccess(false);
			return json;
		}
		
		Model model = new Model();
		
		try {
			List<Input> inputList = inputService.selectInputByTableId(table_id);
			if(param != null && param.equals("change"))//此处判断获取该inputList是不是从用户填写模板界面发起的，如果是，那么就要重新设置input_id，否则主键报错
				inputList = AdminUtils.setInputId(inputList);
			model.setInputList(inputList);//设施inputList
			model.setTable_id(table_id);//设置table_id
			model.setName(tableService.selectTableById(table_id).getName());
			
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}catch (Exception e) {
			logger.error(e.getMessage());
			json.setMsg("获取input未知异常");
			json.setSuccess(false);
			return json;
		}
		
		String token = AdminUtils.getUUID();
		request.getSession().setAttribute(token, token);
		
		json.setMsg(token);
		json.setObj(model);
		json.setSuccess(true);
		return json;
	}
	
	/*该方法参数是table_id，返回inputList模板，之所以不返回model，因为前段用dadagrid显示，要求是list类型*/
	@ResponseBody
	@RequestMapping("/getModelReturnInputList")
	public List<Input> getModelReturnInputList(String table_id){
		
		if(table_id == null){
			return null;
		}
		
		List<Input> inputList = null;
		
		try {
			inputList = inputService.selectInputByTableId(table_id);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			return null;
		}
		
		if(inputList == null)
			return null;
		
		/*由于这里返回的inputList没有顺序，所以这里进行排序*/
		
		
		
		return inputList;
	}
	
	/*该方法是接收一个input对象，然后保存入库，这个对象必须有table_id，不然无法保存*/
	@ResponseBody
	@RequestMapping("/saveInput")
	public Json saveInput(@RequestBody Input input){
		/*这个方法注意了，因为前面可能是更改input然后保存，也有可能是新增input然后是保存
		所以这里要判断是update还是insert操作*/
		Json json = new Json();
		if(input == null){
			json.setMsg("封装input参数失败");
			json.setSuccess(false);
			return json;
		}
		System.out.println(input);
		
		if(input.getInput_id() == null){
			/*input_id为null，说明这个input是新增的，必须为其添加一个input_id*/
			input.setInput_id(AdminUtils.getUUID());
			json = insertInput(input);
		}else{
			/*如果不为null，说明是update操作*/
			json = updateInput(input);
		}
		return json;
	}
	
	/*该方法是接收一个input的id参数集合，然后转为list集合，将里面的所有input_id全部删除*/
	@ResponseBody
	@RequestMapping("/deleteInput")
	public Json deleteInput(String input_ids){
		Json json = new Json();
		
		if(input_ids == null){
			json.setMsg("获取参数input_ids失败");
			json.setSuccess(false);
			return json;
		}
		String[] input_idList =  input_ids.split("-");
		
		for (String input_id : input_idList) {
			if(input_id.length() == 32){
				try {
					inputService.deleteInput(input_id);
				} catch (ServiceException e) {
					logger.error(e.getMessage());
					json.setMsg(e.getMessage());
					json.setSuccess(false);
					return json;
				}catch (Exception e) {
					logger.error(e.getMessage());
					json.setMsg("位置异常");
					json.setSuccess(false);
					return json;
				}
			}
		}
		
		json.setSuccess(true);
		json.setMsg("删除成功");
		return json;
	}
	
	
	@ResponseBody
	@RequestMapping("/updateInput")
	public Json updateInput(@RequestBody Input input){
		Json json = new Json();
		
		if(input == null || input.getInput_id() == null){
			json.setMsg("封装input参数失败");
			json.setSuccess(false);
			return json;
		}
		
		int n = 0;
		try {
			n = inputService.updateInput(input);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}catch (Exception e) {
			logger.error(e.getMessage());
			json.setMsg("未知异常");
			json.setSuccess(false);
			return json;
		}
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/insertInput")
	public Json insertInput(@RequestBody Input input){
		Json json = new Json();
		if(input.getTable_id() == null){
			json.setMsg("该input类没有table_id，无法插入对应的table中");
			json.setSuccess(false);
			return json;
		}
		
		int n = 0;
		try {
			n = inputService.insertInput(input);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}catch (Exception e) {
			logger.error(e.getMessage());
			json.setMsg("insert操作未知异常");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("插入成功 ");
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/testFun")
	public void testFun(int num1,int num2){
		List<Table> n = tableService.selectTableByPage(num1,num2);
		System.out.println(JSON.toJSONString(n, true));
	}
	
	
	@ResponseBody
	@RequestMapping("deleteTableById")
	public Json deleteTableById(String table_id){
		Json json = new Json();
		if(table_id == null){
			json.setMsg("获取table_id参数失败");
			json.setSuccess(false);
			return json;
		}
		
		/*
		 * 删除一个table，必须首先判断该table是否已经被user用户填写过，如果填写了，那么就不能删除
		 * 判断的依据就是查看指定的table_id是否存在于user_table表
		 */
		
		int x = 0;
		try {
			x = userTableService.selectAllUser_TableByTableId(table_id);
		} catch (ServiceException e1) {
			logger.error(e1.getMessage());
			json.setMsg(e1.getMessage());
			json.setSuccess(false);
			return json;
		}
		
		if(x != 0){
			json.setMsg("该表已经被用户填写，不能删除！！！");
			json.setSuccess(false);
			return json;
		}
		
		
		boolean flag = false;
		try {
			flag = tableService.deleteTableById(table_id);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		
		if(!flag){
			json.setMsg("出现未知异常");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("删除成功");
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * 分页查询user_table表
	 * @param page
	 * @param rows
	 * @return
	 * @throws ServiceException
	 */
	@ResponseBody
	@RequestMapping("/getAllUser_Table")
	public Map<String,Object> getAllUser_Table(UserTableSearch userTableSearch,int page,int rows) throws ServiceException{
		// 1 获得table总记录数
		int user_tableTotal = userTableService.getUser_TableTotal(userTableSearch);
		// 2 获得table集合
		List<User_Table> user_tableList = userTableService.getAllUser_Table(userTableSearch,(page-1)*rows, rows);
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total", user_tableTotal);//---------------------------------这里要查询真实数据库中的总记录

		map.put("rows", user_tableList);
		return map;
	}
	
	@ResponseBody
	@RequestMapping("/exportToExcel")
	public Json exportToExcel(@RequestBody 	List<User_Table> user_tableList,String tableName,HttpServletRequest request){
		Json json = new Json();
		if(user_tableList == null || tableName == null){
			json.setMsg("封装userid-tableid or tableName失败");
			json.setSuccess(false);
			return json;
		}
		
		AdminGodownExcelSession modelSession = new AdminGodownExcelSession();
		String admin_uuid_list = AdminUtils.getUUID();
		modelSession.setListkey(admin_uuid_list);
		modelSession.setTablenamekey(tableName);
		request.getSession().setAttribute(admin_uuid_list, user_tableList);
		request.getSession().setAttribute(modelSession.getTablenamekey(), tableName);
		
		json.setObj(modelSession);
		json.setMsg("存入session中成功");
		json.setSuccess(true);
		return json;
	}
	
	
	@ResponseBody
	@RequestMapping("/godownExcel")
	public Json godownExcel(String filename,String sessionid,HttpServletRequest request,HttpServletResponse response){
		Json json = new Json();
		if(filename == null || sessionid == null){
			json.setMsg("获取session中的excel参数失败");
			json.setSuccess(false);
			return json;
		}
		List<User_Table> modelSession = null;
		String name = null;
		try{
			modelSession = (List<User_Table>)request.getSession().getAttribute(sessionid);
			name = (String)request.getSession().getAttribute(filename);
		}catch(Exception e){
			logger.error(e.getMessage());
			json.setMsg("转换session中的excel参数失败");
			json.setSuccess(false);
			return json;
		}
		request.getSession().removeAttribute(sessionid);
		request.getSession().removeAttribute(filename);
		
		boolean flag = false;
		try {
			flag = exportToExcelService.exportToExcel(modelSession,name,response);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			return json;
		}
		if(!flag){
			json.setMsg("Controller 导出失败，异常未知");
			json.setSuccess(false);
			return json;
		}
		
		
		
		return null;
	}

	
	@ResponseBody
	@RequestMapping("/validateExcelName")
	public Json validateExcelName(String excelName){
		Json json = new  Json();
		if(excelName == null){
			json.setMsg("获取excelName失败");
			json.setSuccess(false);
			return json;
		}
		//获取文件完整路径
		String paht1 = AdminUtils.getExcelExportPathPath("excelExportPath");
		File file = new File( paht1 + excelName + ".xls");
		if(file.exists()){
			json.setMsg("yes");
			json.setSuccess(true);
			return json;
		}else{
			json.setMsg("no");
			json.setSuccess(true);
			return json;
		}
	}
	
	@ResponseBody
	@RequestMapping("/getInputByTableAndUserId")
	public Json getInputByTableAndUserId(String table_id,String user_id){
		Json json = new Json();
		if(table_id == null || user_id == null){
			json.setMsg("获取table_id or user_id 参数失败");
			json.setSuccess(true);
			return json;
		}
		
		Model model = new Model();
		//封装model数据
		model.setTable_id(table_id);
		model.setUser_id(user_id);
		Table table = tableService.selectTableById(table_id);
		if(table == null){
			json.setMsg("根据table_id查询table失败");
			json.setSuccess(false);
			return json;
		}
		model.setName(table.getName());
		List<Input> inputList = null;
		try {
			inputList= inputService.selectInputByUserIdAndTableId(table_id, user_id);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			json.setMsg("根据table_id和user_id查询input集合失败");
			json.setSuccess(false);
			return json;
		}
		model.setInputList(inputList);
		
		json.setObj(model);
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/getTableByName")
	public List<TableIdAndName> getTableByName(String name){
		System.out.println("hello ");
		List<TableIdAndName> tt = new ArrayList<TableIdAndName>();
		TableIdAndName t = new TableIdAndName();
		for(int i=0;i<20;i++){
			t.setTable_id("table"+i);t.setName("name"+i);
			tt.add(t);
		}
		return tt;
	}
	
	
}






















