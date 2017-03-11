package cn.edu.sspu.dao.mapper;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.models.User_Table;

public interface User_TableMapper {
	public User_Table selectUser_TableByUserId(String user_id);
	
	public User_Table selectUser_TableByTableId(String table_id);
	
	public int insertUser_Table(User_Table user_table);
	
	public int deleteUser_TableByUserIdAndTableId(@Param("table_id")String table_id,@Param("user_id")String user_id);
	
	public int selectAllUser_TableByTableId(String table_id);

}
