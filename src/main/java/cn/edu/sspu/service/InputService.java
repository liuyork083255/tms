package cn.edu.sspu.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Input;

public interface InputService {
	public List<Input> selectInputByTableId(String table_id) throws ServiceException;

	public List<Input> selectInputByUserIdAndTableId(String table_id,String user_id) throws ServiceException;

	public int deleteInput(String input_id) throws ServiceException;
	
	public int updateInput(Input input) throws ServiceException;
	
	public int insertInput(Input input) throws ServiceException;
	
	public int selectInputTimesMax(String table_id,String user_id);
	
	public List<Integer> selectTimesAllValue(String table_id,String user_id);
	
	public List<Input> selectInputByUserIdAndTableIdAndTimes(String table_id,String user_id,int times);
	
	public List<Object> selectAllInputByTimes(String table_id,String user_id);

	public List<Object> getAllWriteTableByTableId(String table_id,String user_id);
}
