package cn.edu.sspu.dao.mapper;

import cn.edu.sspu.models.Table;

public interface TableMapper {
	public Table selectTableById(String table_id);
	
	public Table selectTableByName(String name);
}
