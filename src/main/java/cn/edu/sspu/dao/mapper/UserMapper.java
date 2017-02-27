package cn.edu.sspu.dao.mapper;

import cn.edu.sspu.models.User;


public interface UserMapper {
	
	public User selectUserByName(String name);
	
	public User selectUserById(String user_id);

}
