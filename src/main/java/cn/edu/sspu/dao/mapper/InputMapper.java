package cn.edu.sspu.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.models.Input;

public interface InputMapper {
	
	public List<Input> selectInputByTableId(String table_id);
	
	public List<Input> selectInputByTableIdUserIdNotNull(String table_id);

	public List<Input> selectInputByUserIdAndTableId(@Param("table_id")String table_id,@Param("user_id") String user_id);

	public Input selectInputById(String input_id);
	
	public int deleteInput(String input_id);
	
	public int updateInput(Input input);
	
	public int insertInput(Input input);
	
	public int updateInputValueById(Input input);
	
	//这里网上说返回值是Integer类型，先做一个实验，看看int是否可以
	public int deleteInputByTableIdAndUserId(@Param("table_id")String table_id,@Param("user_id")String user_id,@Param("times")int times);
	
	public int deleteInputByTableId(String table_id);
	
	public int selectInputTimesMax(@Param("table_id")String table_id,@Param("user_id")String user_id);
	
	public List<Integer> selectTimesAllValue(@Param("table_id")String table_id,@Param("user_id")String user_id);
	
	public List<Input> selectInputByUserIdAndTableIdAndTimes(@Param("table_id")String table_id,@Param("user_id") String user_id,@Param("times")int times);
	
	public List<Input> selectInputByUserIdAndTableIdAndTimesIsFile(@Param("table_id")String table_id,@Param("user_id") String user_id,@Param("times")int times);
	
	public List<String> selectHistoryByUserId_InputName(@Param("user_id") String user_id,@Param("name") String name);
	
}
