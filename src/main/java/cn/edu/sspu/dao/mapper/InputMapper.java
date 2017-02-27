package cn.edu.sspu.dao.mapper;

import cn.edu.sspu.models.Input;

public interface InputMapper {
	
	public Input selectInputByName(String name);

	public Input selectInputById(String input_id);
}
