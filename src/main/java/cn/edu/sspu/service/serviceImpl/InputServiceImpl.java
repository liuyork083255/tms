package cn.edu.sspu.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sspu.dao.mapper.InputMapper;
import cn.edu.sspu.dao.mapper.User_TableMapper;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.User_Table;
import cn.edu.sspu.service.InputService;
import cn.edu.sspu.utils.AdminUtils;

@Service
public class InputServiceImpl implements InputService {
	@Autowired
	private InputMapper inputMapper;
    @Autowired
    private User_TableMapper user_TableMapper;
    
    private static Logger logger = LoggerFactory.getLogger(ExportToExcelServiceImpl.class);

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
	}

	public int selectInputTimesMax(String table_id, String user_id) {
		return inputMapper.selectInputTimesMax(table_id, user_id);
	}

	public List<Integer> selectTimesAllValue(String table_id, String user_id) {
		return inputMapper.selectTimesAllValue(table_id,user_id);
	}

	public List<Input> selectInputByUserIdAndTableIdAndTimes(String table_id,String user_id, int times) {
		List<Input> inputList = inputMapper.selectInputByUserIdAndTableIdAndTimes(table_id, user_id, times);
		return inputList;
	}

	public List<Object> selectAllInputByTimes(String table_id, String user_id) {
		List<Integer> intList = inputMapper.selectTimesAllValue(table_id,user_id);
		List<Input> inputList = null;
		List<Object> objectList = new ArrayList<Object>();
		for(Integer i : intList){
			inputList = inputMapper.selectInputByUserIdAndTableIdAndTimes(table_id, user_id, i);
			try {
				Object object = AdminUtils.transferInputToTableId_Nama(inputList);
				objectList.add(object);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return objectList;
		
	}

	public List<Object> getAllWriteTableByTableId(String table_id,String user_id) {
		//显示获取指定的user_table记录
		List<User_Table> usertableList = user_TableMapper.getAllWriteTableByTableIdUserId(table_id, user_id);
		//然后是获取对应的input记录
		List<Object> objectList = new ArrayList<Object>();
		for(User_Table ut : usertableList){
			List<Input> inputList = inputMapper.selectInputByUserIdAndTableIdAndTimes(ut.getTable_id(),ut.getUser_id(), ut.getTimes());
			try {
				Object object = AdminUtils.transferInputToDetail(inputList, ut.getUser_name());
				objectList.add(object);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return objectList;
	}

	public List<Object> selectHistoryByUserId_InputName(String user_id, String name) {
		//首先是获取指定的Value集合
		List<String> sList = inputMapper.selectHistoryByUserId_InputName(user_id, name);
		List<Object> objectList = null;
		if(sList == null || sList.size() == 0)
			return null;
		try {
			objectList = AdminUtils.selectHistoryByUserId_InputName(sList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		return objectList;
	}

	public List<Input> selectInputByUserIdAndTableIdAndTimesIsFile(String table_id, String user_id, int times) {
		return inputMapper.selectInputByUserIdAndTableIdAndTimesIsFile(table_id, user_id, times);
	};

}






















