package cn.edu.sspu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sspu.dao.mapper.UserTableMapper;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.service.UserTableService;
@Service
public class UserTableServiceImpl implements UserTableService{
	@Autowired
	private UserTableMapper userTableMapper;
	
	public List<Table> selectTableUnwriteByUserId(String user_id,int num1,int num2) throws ServiceException{
		List<Table> tableList = userTableMapper.selectTableUnwriteByUserId(user_id,num1,num2);
		
		if(tableList == null)
			throw new ServiceException("查询指定用户未填写的table列表失败");
		return tableList;
	}

	public int selectTableUnwriteTotal(){
		int n = userTableMapper.selectTableUnwriteTotal();
		return n;
	}
}
