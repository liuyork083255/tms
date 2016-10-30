package cn.edu.sspu.dao.mapper;

import cn.edu.sspu.pojo.User;

public interface UserMapper {
	
	public User selectUserByName(String name);

}
