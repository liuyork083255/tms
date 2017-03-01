package cn.edu.sspu.utils;

import java.util.List;
import java.util.UUID;

import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.Model;

public class AdminUtils {

	/* 第一个方法是接收model对象，然后将里面的集合全部转为list，并返回 */
	public static List<Object> modelToList() {
		

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
	
	
	public static Input setInputId(Input input){
		if(input != null){
			input.setInput_id(UUID.randomUUID().toString().replace("-", "").toUpperCase());
		}
		return input;
	}
	
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
}
