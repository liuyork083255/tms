package cn.edu.sspu.utils;

import java.util.ArrayList;
import java.util.List;

import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Select;
import cn.edu.sspu.models.Textarea;

public class AdminUtils {

	/* 第一个方法是接收model对象，然后将里面的集合全部转为list，并返回 */
	public static List<Object> modelToList(Model model, String table_id) {
		List<Object> list = new ArrayList<Object>();
		if (model == null)
			return list;
		List<Input> inputList = model.getInputList();
		List<Select> selectList = model.getSelectList();
		List<Textarea> textareaList = model.getTextareaList();
		if (inputList != null) {
			for (Input i : inputList) {
				i.setTable_id(table_id);
				list.add(i);
			}

		}
		if (selectList != null) {
			for (Select s : selectList) {
				s.setTable_id(table_id);
				list.add(s);
			}

		}
		if (textareaList != null) {
			for (Textarea t : textareaList) {
				t.setTable_id(table_id);
				list.add(t);
			}

		}

		return list;
	}
}
