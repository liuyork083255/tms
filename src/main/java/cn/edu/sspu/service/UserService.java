package cn.edu.sspu.service;

import java.util.List;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.User;


public interface UserService {
	
	public User selectUserByName(String name) throws ServiceException;
	
	public User selectUserById(String user_id) throws ServiceException;
	
	public User selectUserByNameAndPassword(String name,String password) throws ServiceException;
	
	public List<User> getAllUser() throws ServiceException;
	
	public int updateUser(User user) throws ServiceException;
	
	public int deleteUserByName(String name) throws ServiceException;
	
	public int deleteUserById(String user_id) throws ServiceException;
	
	public int insertUser(User user) throws ServiceException;
}
