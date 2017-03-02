package cn.edu.sspu.service;

import java.util.List;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.User;

public interface TableService {
	
	public Table selectTableById(String table_id) throws Exception;
	
	public Table selectTableByName(String name) throws Exception;
	
	public int insertTable(Table table) throws ServiceException;
	
	public boolean insertModel(Model model) throws ServiceException;
	
	public int selectTableTotal();
	
	public List<Table> selectTableByPage(int num1,int num2);
	
	public void testTr(Table table);

}
