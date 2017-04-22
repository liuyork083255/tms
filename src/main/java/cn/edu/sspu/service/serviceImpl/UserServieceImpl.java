package cn.edu.sspu.service.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sspu.dao.mapper.TableMapper;
import cn.edu.sspu.dao.mapper.UserMapper;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.User;
import cn.edu.sspu.service.UserService;
import cn.edu.sspu.utils.AdminUtils;

@Service
public class UserServieceImpl implements UserService{
	@Autowired
	private UserMapper userMapper = null ;
	@Autowired
	private TableMapper tableMapper = null;
	
	private static Logger logger = LoggerFactory.getLogger(ExportToExcelServiceImpl.class);

	public User selectUserByName(String name) throws ServiceException {
		User user = userMapper.selectUserByName(name);
		
		if(user == null){
			throw new ServiceException("按name查找user为空");
		}
		return user;
	}
	
	public User selectUserById(String user_id) throws ServiceException {
		User user = userMapper.selectUserById(user_id);
		
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

	public List<User> getAllUser() throws ServiceException{
		
		List<User> userList = userMapper.getAllUser();
		
		if(userList == null){
			throw new ServiceException("获取所有user为空");
		}
		
		return userList;
		
	}

	public int updateUser(User user)  throws ServiceException{
		int n = userMapper.updateUser(user);
		//n为0 表示update影响的行数，说明update失败
		if(n == 0){
			throw new ServiceException("update user 表失败");
		}
		return n;
	}

	public int deleteUserByName(String name) throws ServiceException {
		int n = 0;
		try{
			n = userMapper.deleteUserByName(name);
		}catch(Exception e){
			logger.error(e.getMessage());
			if(e.getMessage().contains("SQLIntegrityConstraintViolationException")){
				throw new ServiceException("主键 or 外键依赖 异常");
			}
		}
		if(n == 0){
			throw new ServiceException("通过name删除user失败");
		}
		return n;
	}

	public int deleteUserById(String user_id) throws ServiceException {
		int n = 0;
		try{
			n = userMapper.deleteUserById(user_id);
		}catch(Exception e){
			logger.error(e.getMessage());
			if(e.getMessage().contains("SQLIntegrityConstraintViolationException")){
				throw new ServiceException("主键 or 外键依赖 异常");
			}
		}
		if(n == 0){
			throw new ServiceException("通过user_id删除user失败");
		}
		return n;
	}

	public int insertUser(User user) throws ServiceException{
		int n = 0;
		try{
			n = userMapper.insertUser(user);
		}catch(Exception e){
			logger.error(e.getMessage());
			throw new ServiceException("insert 操作未知异常");
		}
		if(n ==  0){
			throw new ServiceException("insert 插入失败");
		}
		return n;
	}

	public int getUserTotal(String name) {
		return userMapper.getUserTotal(name);
	}

	public List<User> selectUserByFilter(String name, int page, int rows) throws ServiceException {
		return userMapper.selectUserByFilter(name, page, rows);
	}

	public User selectUserByIdNotName(String user_id, String name) {
		return userMapper.selectUserByIdNotName(user_id, name);
	}

	public List<Object> getAllUserIdAndName() {
		List<User> userList = userMapper.getAllUser();
		List<Object> objectList = null;
		try {
			objectList = AdminUtils.transferUserToId_Name(userList);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
		}
		return objectList;
	}

	public User selectUserByEmail(String email) {
		return userMapper.selectUserByEmail(email);
	}
	
	

	
}






















