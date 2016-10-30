package cn.edu.sspu.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sspu.dao.mapper.UserMapper;
import cn.edu.sspu.pojo.User;
import cn.edu.sspu.service.UserService;

@Service
public class UserServieceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper = null ;

	public User selectUserByName(String name) {
		System.out.println("the userMapper is" + userMapper);
		return userMapper.selectUserByName(name);
	}

}
