package cn.edu.sspu.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.models.User_Table;
import cn.edu.sspu.models.searchfilter.UserTableSearch;

public interface User_TableMapper {
	public User_Table selectUser_TableByUserId(String user_id);
	
	public User_Table selectUser_TableByTableId(String table_id);
	
	public int insertUser_Table(User_Table user_table);
	
	public int deleteUser_TableByUserIdAndTableId(@Param("table_id")String table_id,@Param("user_id")String user_id);
	
	public int selectAllUser_TableByTableId(String table_id);
	
	public List<User_Table> getAllUser_Table(@Param("userTableSearch")UserTableSearch userTableSearch, @Param("num1")int num1,@Param("num2") int num2);
	
	public int getUser_TableTotal();

}
