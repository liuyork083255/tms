package cn.edu.sspu.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sspu.dao.mapper.UserMapper;
import cn.edu.sspu.exception.ExceptionType;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.User;
import cn.edu.sspu.service.UserService;

@Service
public class UserServieceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper = null ;

	public User selectUserByName(String name) throws ServiceException {
		User user = userMapper.selectUserByName(name);
		
		if(user == null){
			throw new ServiceException("按name查找user为空");
		}
		return user;
	}
	
	public User selectUserById(String user_id) throws ServiceException {
		User user = userMapper.selectUserByName(user_id);
		
		if(user == null){
			throw new ServiceException("按id查找user为空");
		}
		
		return user;
	}
	
	public User selectUserByNameAndPassword(String name,String password) throws ServiceException{
		User user = userMapper.selectUserByNameAndPassword(name,password);
		
		if(user == null){
			throw new ServiceException("按name和password查找user为空");
		}
		return user;
		
	}
}
