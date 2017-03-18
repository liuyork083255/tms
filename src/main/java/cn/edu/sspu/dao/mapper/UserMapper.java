package cn.edu.sspu.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.models.User;


public interface UserMapper {
	
	public User selectUserByName(String name);
	
	public User selectUserById(String user_id);

	public User selectUserByNameAndPassword(@Param("name")String name,@Param("password") String password);
	
	public List<User> getAllUser();
	
	public int updateUser(User user);
	
	public int deleteUserByName(String name);
	
	public int deleteUserById(String user_id);

	public int insertUser(User user);
	
	public int getUserTotal(@Param("name")String name);
	
	public List<User> selectUserByFilter(@Param("name")String name,@Param("page")int page,@Param("rows")int rows);
	
	public User selectUserByIdNotName(@Param("user_id")String user_id,@Param("name")String name);
}
