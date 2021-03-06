package cn.edu.sspu.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.File;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.User_Table;
import cn.edu.sspu.models.searchfilter.UserTableSearch;

public interface UserTableService {
	public List<Table> selectTableUnwriteByUserId(String user_id,int num1,int num2) throws ServiceException;
	
	public List<Table> selectTableWriteByUserId(String user_id,int num1,int num2) throws ServiceException;
	
	public int selectTableWriteTotal(String user_id);
	
	public int selectTableUnwriteTotal(String user_id);
	
	public boolean saveUserEditModel(Model model,HttpSession session) throws ServiceException;
	
	
	public boolean updataInputValueById(String input_id,String input_value,String type,File file) throws ServiceException;
	
	
	public boolean deleteUserWriteTable(String table_id,String user_id,String times) throws ServiceException;
	
	public int selectAllUser_TableByTableId(String table_id) throws ServiceException;
	
	public List<User_Table> getAllUser_Table(UserTableSearch userTableSearch,int num1,int num2) throws ServiceException;
	
	public int getUser_TableTotal(UserTableSearch userTableSearch) throws ServiceException;
	
}
