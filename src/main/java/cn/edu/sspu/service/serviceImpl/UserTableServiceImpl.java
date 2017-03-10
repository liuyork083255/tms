package cn.edu.sspu.service.serviceImpl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.edu.sspu.dao.mapper.FileMapper;
import cn.edu.sspu.dao.mapper.InputMapper;
import cn.edu.sspu.dao.mapper.UserMapper;
import cn.edu.sspu.dao.mapper.UserTableMapper;
import cn.edu.sspu.dao.mapper.User_TableMapper;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.File;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.User;
import cn.edu.sspu.models.User_Table;
import cn.edu.sspu.service.UserTableService;
import cn.edu.sspu.utils.AdminUtils;
@Service
public class UserTableServiceImpl implements UserTableService{
	//测试事务注入类
	@Autowired
	private DataSourceTransactionManager trManager;

	@Autowired
	private UserTableMapper userTableMapper;
	
	@Autowired
	private InputMapper inputMapper;
	
	@Autowired
	private FileMapper fileMapper;
	
	@Autowired
	private User_TableMapper user_TableMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	public List<Table> selectTableUnwriteByUserId(String user_id,int num1,int num2) throws ServiceException{
		List<Table> tableList = userTableMapper.selectTableUnwriteByUserId(user_id,num1,num2);
		
		if(tableList == null)
			throw new ServiceException("查询指定用户未填写的table列表失败");
		return tableList;
	}
	

	public List<Table> selectTableWriteByUserId(String user_id, int num1,int num2) throws ServiceException {
		List<Table> tableList = userTableMapper.selectTableWriteByUserId(user_id,num1,num2);
		
		if(tableList == null)
			throw new ServiceException("查询指定用户已填写的table列表失败");
		return tableList;
	}

	public int selectTableWriteTotal(String user_id) {
		int n = userTableMapper.selectTableWriteTotal(user_id);
		return n;
	}

	public int selectTableUnwriteTotal(String user_id){
		int n = userTableMapper.selectTableUnwriteTotal(user_id);
		return n;
	}

	public boolean saveUserEditModel(Model model,HttpSession session) throws ServiceException {
		// 得到事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = trManager.getTransaction(def);
		
		//注意：这要插入三张表，分别是file user_table input
		//这里不用一次性插入ListInput集合，而是一条一条判断插入
		List<Input> inputList = model.getInputList();
		try {
			for(Input input : inputList){
				if(input.getType().equals("file")){//如果是file类型，说明还要插入file表
					File sessionFile = (File)session.getAttribute(input.getInput_id());//获取session中input对应的file
					if(sessionFile == null){
						throw new ServiceException("获取Session中input对应的file失败");
					}
					
					input.setValue(sessionFile.getFilename());//这里设置id其实就是 input的id
					int fileInsertFlag = fileMapper.insertFile(sessionFile);//插入file表
					if(fileInsertFlag == 0)//插入失败抛出异常
						throw new ServiceException("插入file表失败");
					
					int inputInsertFlag = inputMapper.insertInput(input);//插入input表
					if(inputInsertFlag == 0)//插入失败抛出异常
						throw new ServiceException("插入input表失败");
					
				}else{//如果不是file类型，那就直接插入input表就可以了
					int inputInsertFlag = inputMapper.insertInput(input);//插入input表
					if(inputInsertFlag == 0)//插入失败抛出异常
						throw new ServiceException("插入input表失败");
				}
			}
			//如果上面都没有出现异常，那么说明input表和file表都插入成功，接下来就是插入user_table表
			//首先是封装数据
			User_Table user_table = new User_Table();
			user_table.setTable_id(model.getTable_id());
			user_table.setUser_id(model.getUser_id());
			//获取username，因为model中没有，所以请求数据库查询
			User user = userMapper.selectUserById(model.getUser_id());
			if(user == null)
				throw new ServiceException("根据user_id查询user失败");
			user_table.setUser_name(user.getName());
			user_table.setTable_name(model.getName());
			user_table.setUser_table_time(AdminUtils.getCurrentTime());
			
			int user_tableInertFlag = user_TableMapper.insertUser_Table(user_table);//插入user_table表
			if(user_tableInertFlag == 0)//插入失败抛出异常
				throw new ServiceException("插入user_table表失败");
			
			trManager.commit(status);//完成提交
		} catch (ServiceException e) {
			trManager.rollback(status);
			throw new ServiceException(e.getMessage());
		} catch (Exception e) {
			trManager.rollback(status);
			throw new ServiceException(e.getMessage());
		}
		return true;
	}


	public boolean updataInputValueById(String input_id, String input_value,String type, File file) throws ServiceException {
		// 得到事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = trManager.getTransaction(def);
		try {
			
			Input input = inputMapper.selectInputById(input_id);
			input.setValue(input_value);
			System.out.println(JSON.toJSONString(input, true));
			if(type.equals("file")){
				int n = fileMapper.updateFile(file);//插入file表
				if(n == 0)
					throw new ServiceException("file表失败");
				
				int m = inputMapper.updateInputValueById(input);
				if(m == 0)
					throw new ServiceException("file表失败");
			}else{
				int m = inputMapper.updateInputValueById(input);
				if(m == 0)
					throw new ServiceException("file表失败");
			}
			
			trManager.commit(status);//完成提交
		} catch (ServiceException e) {
			trManager.rollback(status);
			throw new ServiceException(e.getMessage());
		} catch (Exception e) {
			trManager.rollback(status);
			throw new ServiceException(e.getMessage());
		}
		return true;
	}

}






















