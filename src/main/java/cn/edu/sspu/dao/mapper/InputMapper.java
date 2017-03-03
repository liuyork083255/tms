package cn.edu.sspu.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.models.Input;

public interface InputMapper {
	
	public List<Input> selectInputByTableId(String table_id);

	public List<Input> selectInputByUserIdAndTableId(@Param("table_id")String table_id,@Param("user_id") String user_id);

	public int deleteInput(String input_id);
	
	public int updateInput(Input input);
	
	public int insertInput(Input input);
	
	

}
