package cn.edu.sspu.service;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.User;


public interface UserService {
	
	public User selectUserByName(String name) throws ServiceException;
	
	public User selectUserById(String user_id) throws ServiceException;
	
	public User selectUserByNameAndPassword(String name,String password) throws ServiceException;
	
}
