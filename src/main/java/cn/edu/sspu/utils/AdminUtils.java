package cn.edu.sspu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.File;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.InputName_Value;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.TableIdAndName;
import cn.edu.sspu.models.User;



public class AdminUtils {
	
	private static Logger logger = LoggerFactory.getLogger(AdminUtils.class);
	
	/* 第一个方法是接收model对象，然后将里面的集合全部转为list，并返回 */
	public static List<Object> modelToList() {
		return null;
	}
	
	public static List<Input> setAllnputUserId(List<Input> inputList,String user_id){
		if(inputList != null && user_id != null){
				for(Input input : inputList){
					input.setUser_id(user_id);
				}
			}
		return inputList;
	}
	
	
	public static String getFileUploadPath(String pathName){
		InputStream is = AdminUtils.class.getClassLoader().getResourceAsStream("db.properties");
		if(is != null){
			Properties pps = new Properties();
			try {
				pps.load(is);
				return (String)pps.get(pathName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		return null;
	}
	
	public static String getAdminEmail(String adminEmail){
		InputStream is = AdminUtils.class.getClassLoader().getResourceAsStream("db.properties");
		if(is != null){
			Properties pps = new Properties();
			try {
				pps.load(is);
				return (String)pps.get(adminEmail);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		return null;
	}
	
	public static String getExcelExportPathPath(String pathName){
		InputStream is = AdminUtils.class.getClassLoader().getResourceAsStream("db.properties");
		if(is != null){
			Properties pps = new Properties();
			try {
				pps.load(is);
				return (String)pps.get(pathName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		return null;
	}
	
	public static String getProjectIP(String projectIPKey){
		InputStream is = AdminUtils.class.getClassLoader().getResourceAsStream("db.properties");
		if(is != null){
			Properties pps = new Properties();
			try {
				pps.load(is);
				return (String)pps.get(projectIPKey);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		return null;
	}
	
	
	//该方法参数是Model类型，作用是设置model中的table_id和input_id,并返回model
	public static Model setModelIdAndInputId(Model model){
		if(model != null){
			model.setTable_id(UUID.randomUUID().toString().replace("-", "").toUpperCase());
			if(model.getInputList() != null){
				for(Input input : model.getInputList()){
					input.setTable_id(model.getTable_id());
					input.setInput_id(UUID.randomUUID().toString().replace("-", "").toUpperCase());
				}
			}
		}
		return model;
	}
	
	public static List<Input> setInputId(List<Input> inputList){
		if(inputList != null){
				for(Input input : inputList){
					input.setInput_id(UUID.randomUUID().toString().replace("-", "").toUpperCase());
				}
			}
		return inputList;
	}
	
	
	public static Input setInputId(Input input){
		if(input != null){
			input.setInput_id(UUID.randomUUID().toString().replace("-", "").toUpperCase());
		}
		return input;
	}
	
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
	
	public static String getCurrentTime(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	
	public static Object transferInputToTableId_Nama(List<Input> inputList) throws ClassNotFoundException{
		if(inputList == null || inputList.size() == 0)
			return null;
		HashMap<String,Class<?>> propertyMap = new HashMap<String,Class<?>>();
		for(Input input : inputList){
			propertyMap.put(input.getName(), Class.forName("java.lang.String"));
		}
		propertyMap.put("times", Class.forName("java.lang.Integer"));
		DynamicBean bean = new DynamicBean(propertyMap);
		for(Input input : inputList){
			bean.setValue(input.getName(), input.getValue());
		}
		bean.setValue("times", inputList.get(0).getTimes());
		Object object = bean.getObject(); 

		return object;
	}

	
	public static List<Object> transferTableToId_Name(List<Table> tableList) throws ClassNotFoundException{
		HashMap<String,Class<?>> propertyMap = new HashMap<String,Class<?>>();
		List<Object> objectList = new ArrayList<Object>();
		for(Table table : tableList){
			propertyMap.put("table_id", Class.forName("java.lang.String"));
			propertyMap.put("name", Class.forName("java.lang.String"));
			DynamicBean bean = new DynamicBean(propertyMap);
			bean.setValue("table_id", table.getTable_id());
			bean.setValue("name", table.getName());
			Object object = bean.getObject();
			objectList.add(object);
		}
		
		return objectList;
	}
	
	public static List<Object> transferUserToId_Name(List<User> userList) throws ClassNotFoundException{
		HashMap<String,Class<?>> propertyMap = new HashMap<String,Class<?>>();
		List<Object> objectList = new ArrayList<Object>();
		for(User user : userList){
			propertyMap.put("user_id", Class.forName("java.lang.String"));
			propertyMap.put("name", Class.forName("java.lang.String"));
			DynamicBean bean = new DynamicBean(propertyMap);
			bean.setValue("user_id", user.getUser_id());
			bean.setValue("name", user.getName());
			Object object = bean.getObject();
			objectList.add(object);
		}
		
		return objectList;
	}
	
	public static Object transferInputToDetail(List<Input> inputList,String username) throws ClassNotFoundException{
		if(inputList == null || inputList.size() == 0)
			return null;
		
		HashMap<String,Class<?>> propertyMap = new HashMap<String,Class<?>>();
		for(Input input : inputList){
			propertyMap.put(input.getName(), Class.forName("java.lang.String"));
		}
		
		propertyMap.put("tableid", Class.forName("java.lang.String"));
		propertyMap.put("userid", Class.forName("java.lang.String"));
		propertyMap.put("username", Class.forName("java.lang.String"));
		propertyMap.put("times", Class.forName("java.lang.Integer"));
		
		DynamicBean bean = new DynamicBean(propertyMap);
		for(Input input : inputList){
			if(input.getType().equalsIgnoreCase("file"))
				bean.setValue(input.getName(), input.getValue()+"_"+input.getInput_id());
			else
				bean.setValue(input.getName(), input.getValue());
		}
		
		bean.setValue("tableid", inputList.get(0).getTable_id());
		bean.setValue("userid", inputList.get(0).getUser_id());
		bean.setValue("username", username);
		bean.setValue("times", inputList.get(0).getTimes());
		
		Object object = bean.getObject(); 

		return object;
	}
	
	
	public static void setInputTimes(List<Input> inputList, int max) {
		for(Input input : inputList)
			input.setTimes(max);
		
	}

	public static List<Object> selectHistoryByUserId_InputName(List<String> sList) throws Exception {
		HashMap<String,Class<?>> propertyMap = new HashMap<String,Class<?>>();
		List<Object> objectList = new ArrayList<Object>();
		for(String s : sList){
			propertyMap.put("value1", Class.forName("java.lang.String"));
			propertyMap.put("value2", Class.forName("java.lang.String"));
			DynamicBean bean = new DynamicBean(propertyMap);
			bean.setValue("value1", s);
			bean.setValue("value2", s);
			Object object = bean.getObject();
			objectList.add(object);
		}
		
		return objectList;
	}

	public static List<Object> transferFileToDetail(List<File> fileList,Map<String, Input> map)  throws Exception{
		HashMap<String,Class<?>> propertyMap = new HashMap<String,Class<?>>();
		List<Object> objectList = new ArrayList<Object>();
		for(File file : fileList){
			propertyMap.put("input_id", Class.forName("java.lang.String"));
			propertyMap.put("file_name", Class.forName("java.lang.String"));
			propertyMap.put("file_type", Class.forName("java.lang.String"));
			propertyMap.put("file_user_name", Class.forName("java.lang.String"));
			propertyMap.put("file_upload_time", Class.forName("java.lang.String"));
			DynamicBean bean = new DynamicBean(propertyMap);
			bean.setValue("input_id", file.getInput_id());
			bean.setValue("file_name", file.getFilename().substring(0, file.getFilename().lastIndexOf(".")));
			bean.setValue("file_type", file.getFiletype());
			bean.setValue("file_upload_time", file.getUploadtime());
		}
		return null;
	}
	
	
	public static String getRedisHost(String host){
		InputStream is = AdminUtils.class.getClassLoader().getResourceAsStream("db.properties");
		if(is != null){
			Properties pps = new Properties();
			try {
				pps.load(is);
				return (String)pps.get(host);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public static int getRedisIP(String port){
		InputStream is = AdminUtils.class.getClassLoader().getResourceAsStream("db.properties");
		if(is != null){
			Properties pps = new Properties();
			try {
				pps.load(is);
				return Integer.parseInt((String)pps.get(port));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 6379;
			}
		}
		return 6379;
	}
	
	public static int getRedisExpireTime(String expireTime){
		InputStream is = AdminUtils.class.getClassLoader().getResourceAsStream("db.properties");
		if(is != null){
			Properties pps = new Properties();
			try {
				pps.load(is);
				return Integer.parseInt((String)pps.get(expireTime));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 1;
			}
		}
		return 1;
	}
	
	public static void deleteFileByInput(Input input) throws Exception{
		try{
		String path = AdminUtils.getFileUploadPath("fileUploadPath");
		
		String type = input.getValue().substring(input.getValue().lastIndexOf("."));
		
		
		java.io.File file = new java.io.File(path + input.getUser_id() + "/" + input.getInput_id() + type);
		
		System.out.println(file.getAbsolutePath());
		System.out.println(file.exists());
		if(file.exists())
			file.delete();
		}catch(Exception e){
			logger.error("根据input 删除硬盘文件失败   异常信息为 ： " + e.getMessage());
			throw new ServiceException("根据input 删除硬盘文件失败");
		}
	}
	
}






























