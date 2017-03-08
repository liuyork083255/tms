package cn.edu.sspu.dao.mapper;

import cn.edu.sspu.models.User_Table;

public interface User_TableMapper {
	public User_Table selectUser_TableByUserId(String user_id);
	
	public User_Table selectUser_TableByTableId(String table_id);
	
	public int insertUser_Table(User_Table user_table);

}
