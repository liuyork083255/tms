package cn.edu.sspu.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.models.Table;

public interface TableMapper {
	public Table selectTableById(String table_id);
	
	public Table selectTableByName(String name);
	
	public int insertTable(Table table);
	
	public int selectTableTotal();
	
	public List<Table> selectTableByPage(@Param("num1")int num1,@Param("num2") int num2);
	
}
