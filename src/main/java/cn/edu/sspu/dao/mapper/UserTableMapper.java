package cn.edu.sspu.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.models.Table;

public interface UserTableMapper {
	public List<Table> selectTableUnwriteByUserId(@Param("user_id")String user_id,@Param("num1")int num1,@Param("num2") int num2);
	
	public int selectTableUnwriteTotal();
}
