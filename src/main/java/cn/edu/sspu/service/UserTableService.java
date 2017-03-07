package cn.edu.sspu.service;

import java.util.List;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Table;

public interface UserTableService {
	public List<Table> selectTableUnwriteByUserId(String user_id,int num1,int num2) throws ServiceException;
	
	public int selectTableUnwriteTotal();
}
