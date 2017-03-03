package cn.edu.sspu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sspu.dao.mapper.InputMapper;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.service.InputService;

@Service
public class InputServiceImpl implements InputService {
	
	@Autowired
	private InputMapper inputMapper;

	public List<Input> selectInputByTableId(String table_id) throws ServiceException {
		
		List<Input> inputList = inputMapper.selectInputByTableId(table_id);
		
		if(inputList == null)
			throw new ServiceException("根据table_id查询input集合失败");
		return inputList;
	}

	public List<Input> selectInputByUserIdAndTableId(String table_id,String user_id) throws ServiceException {
		
		List<Input> inputList = inputMapper.selectInputByUserIdAndTableId(table_id,user_id);
		
		if(inputList == null)
			throw new ServiceException("根据table_id查询input集合失败");
		
		return inputList;
	}
	
	public int deleteInput(String input_id) throws ServiceException{
		
		int n = inputMapper.deleteInput(input_id);
		
		if(n == 0)
			throw new ServiceException("删除input 失败");
		return n;
	};
	
	public int updateInput(Input input) throws ServiceException{
		int n = inputMapper.updateInput(input);
		
		if(n == 0)
			throw new ServiceException("更新input 失败");
		return n;
	};
	
	public int insertInput(Input input) throws ServiceException{
		int n = inputMapper.insertInput(input);
		
		if(n == 0)
			throw new ServiceException("插入input 失败");
		return n;
	};

}
