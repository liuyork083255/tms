package cn.edu.sspu.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.models.User;


public interface UserMapper {
	
	public User selectUserByName(String name);
	
	public User selectUserById(String user_id);

	public User selectUserByNameAndPassword(@Param("name")String name,@Param("password") String password);
	
	public List<User> getAllUser();

}
