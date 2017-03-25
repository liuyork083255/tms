package cn.edu.sspu.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.User;



public interface UserService {
	
	public User selectUserByName(String name) throws ServiceException;
	
	public User selectUserById(String user_id) throws ServiceException;
	
	public User selectUserByEmail(String email);
	
	public User selectUserByNameAndPassword(String name,String password) throws ServiceException;
	
	public List<User> getAllUser() throws ServiceException;
	
	public int updateUser(User user) throws ServiceException;
	
	public int deleteUserByName(String name) throws ServiceException;
	
	public int deleteUserById(String user_id) throws ServiceException;
	
	public int insertUser(User user) throws ServiceException;

	public int getUserTotal(String name);
	
	public List<User> selectUserByFilter(String name,int page,int rows) throws ServiceException;
	
	public User selectUserByIdNotName(String user_id,String name);
	
	public List<Object> getAllUserIdAndName();

}
