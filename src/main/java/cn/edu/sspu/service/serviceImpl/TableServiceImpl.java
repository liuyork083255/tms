package cn.edu.sspu.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import cn.edu.sspu.dao.mapper.InputMapper;
import cn.edu.sspu.dao.mapper.TableMapper;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.User;
import cn.edu.sspu.service.TableService;
import cn.edu.sspu.utils.AdminUtils;

@Service
public class TableServiceImpl implements TableService{
	
	//测试事务注入类
	@Autowired
	private DataSourceTransactionManager trManager;
	
	@Autowired
	private TableMapper tableMapper = null;
	
	@Autowired
	private InputMapper inputMapper;

	public Table selectTableById(String table_id){
		Table table = tableMapper.selectTableById(table_id);
		return table;
	}

	public Table selectTableByName(String name) throws Exception{
		Table table = tableMapper.selectTableByName(name);
		return table;
	}

	public int insertTable(Table table) throws ServiceException {
		int n = 0;
		try{
			n = tableMapper.insertTable(table);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("insert 操作未知异常");
		}
		if(n ==  0){
			throw new ServiceException("insert 插入失败");
		}
		return n;
	}
	
	public boolean insertModel(Model model) throws ServiceException {
		/*
		 * 首先拿到model，不要校验了，交给controller校验
		 * 其实 是分为两步骤
		 * 1 先是从model拿出table，单独插入
		 * 2 是拿出input集合插入
		 * 		中间用事务控制
		 */
		//1 创建和封装table
		Table table = new Table();
		table.setTable_id(model.getTable_id());
		table.setName(model.getName());
		table.setCreatetime(AdminUtils.getCurrentTime());
		
		//2 得到input集合
		List<Input> inputList = model.getInputList();
		
		//3 得到事务控制对象
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = trManager.getTransaction(def);
		
		//4 插入数据
		try {
			tableMapper.insertTable(table);//插入table
			insertInputList(inputList);//插入table对应的input集合
			
			trManager.commit(status);
		} catch (Exception e) {
			trManager.rollback(status);
			throw new ServiceException("插入model异常 ");
		}
		
		
		return true;
	}

	
	public int selectTableTotal(){
		return tableMapper.selectTableTotal();
	}
	
	
	public List<Table> selectTableByPage(int num1,int num2){
		return tableMapper.selectTableByPage(num1,num2);
	}
	
	
	public int updateTable(Table table) throws ServiceException {
		
		int n = tableMapper.updateTable(table);
		
		if(n == 0)
			throw new ServiceException("update Table表失败");
		
		return n;
	}
	
	
	public int insertInputList(List<Input> inputList){
		
		tableMapper.insertInputList(inputList);
		
		return 0;
	}
	
	public void testTr(Table table){
		if(trManager != null){
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus status = trManager.getTransaction(def);
			
			try {
				
				tableMapper.insertTable(table);
				int i = 2;
				i = 3 / 0;
				trManager.commit(status);
				
			} catch (Exception e) {
				System.out.println(e.getClass());
				e.printStackTrace();
				trManager.rollback(status);
			}
		}else
			System.out.println("trManager 注入失败");
		
		
	}

	public boolean deleteTableById(String table_id) throws ServiceException {
		//获取事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = trManager.getTransaction(def);
		
		try {
			//要删除table，根据主外键，首先必须要删除其对应的input
			int x = inputMapper.deleteInputByTableId(table_id);
			if(x == 0)
				throw new ServiceException("删除table对应的input集合失败");
			
			int y = tableMapper.deleteTableById(table_id);
			if(y == 0)
				throw new ServiceException("删除table失败");
			
			trManager.commit(status);
		} catch (Exception e) {
			trManager.rollback(status);
			e.printStackTrace();
			throw new ServiceException("删除未知异常 ");
		}
		
		return true;

	}


}
