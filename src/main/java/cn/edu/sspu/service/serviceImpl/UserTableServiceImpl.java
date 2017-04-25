package cn.edu.sspu.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import cn.edu.sspu.models.searchfilter.UserTableSearch;
import cn.edu.sspu.service.UserTableService;
import cn.edu.sspu.utils.AdminUtils;
import cn.edu.sspu.utils.RedisUtils;
import redis.clients.jedis.Jedis;
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
	
	private static Logger logger = LoggerFactory.getLogger(ExportToExcelServiceImpl.class);
	
	
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
		
		//创建一个List存放file的inputid，因为一个表单里面可能有多个文件上传字段，所以这里需要一次性获取，
		//因为要结合事务来处理，也就是需要等到表插入成功后才可以写入硬盘，所以这里采用这种策略
		List<String> rediskeyList = new ArrayList<String>();
		
		try {
			for(Input input : inputList){
				if(input.getType().equals("file")){      //如果是file类型，说明还要插入file表
					
					//将key存入list中
					rediskeyList.add(input.getInput_id());
					
					File sessionFile = (File)session.getAttribute(input.getInput_id());//获取session中input对应的file
					if(sessionFile == null){
						throw new ServiceException("获取Session中input对应的file失败");
					}
					
					//这里必须先插入input，然后再插入file，因为file中的input_id是input的外键
					
					input.setValue(sessionFile.getFilename());//这里设置id其实就是 input的id
					int inputInsertFlag = inputMapper.insertInput(input);//插入input表
					if(inputInsertFlag == 0)//插入失败抛出异常
						throw new ServiceException("插入input表失败");
					
					
					int fileInsertFlag = fileMapper.insertFile(sessionFile);//插入file表
					if(fileInsertFlag == 0)//插入失败抛出异常
						throw new ServiceException("插入file表失败");
					
				}else{//如果不是file类型，那就直接插入input表就可以了
					int inputInsertFlag = inputMapper.insertInput(input);//插入input表
					if(inputInsertFlag == 0)//插入失败抛出异常
						throw new ServiceException("插入input表失败");
				}
			}
			//如果上面都没有出现异常，那么说明input表和file表都插入成功，接下来就是插入user_table表
			//注意：如果是第一次填表，那么就插入user_table表，如果不是，那么就不插入

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
			user_table.setTimes(model.getInputList().get(0).getTimes());
			
			int user_tableInertFlag = user_TableMapper.insertUser_Table(user_table);//插入user_table表
			if(user_tableInertFlag == 0)//插入失败抛出异常
				throw new ServiceException("插入user_table表失败");

			
			//以上步骤都完成了说明表插入都是成功的，那么就可以写入redis文件到硬盘上了
			
			try{
				for(String inputid : rediskeyList ){
					File sessionFile = (File)session.getAttribute(inputid);
					RedisUtils.outFile(sessionFile.getUser_id(),sessionFile.getInput_id(),sessionFile.getFiletype());
					session.removeAttribute(inputid);
				}
			}catch(Exception e){
				logger.error("执行获取redis文件并存入硬盘操作异常" + "   ====   " + e.getMessage() );
				throw new ServiceException(e.getMessage());
			}
			
			trManager.commit(status);//完成提交
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			trManager.rollback(status);
			throw new ServiceException(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
			trManager.rollback(status);
			throw new ServiceException(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			trManager.rollback(status);
			throw new ServiceException(e.getMessage());
		}
		return true;
	}


	public boolean deleteUserWriteTable(String table_id, String user_id,String times) throws ServiceException {
		// 得到事务
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = trManager.getTransaction(def);
		try {
			//首先是获取要删除的表单的里面是file类型的字段，因为还要删除硬盘上的文件
			List<Input> inputList = inputMapper.selectInputByUserIdAndTableIdAndTimesIsFile(table_id, user_id, Integer.parseInt(times));
			
			/*
			 * 由于还要删除input中存在file类型对应的file表，这里采用的是数据库的联动来实现联动删除
			 */
			//1 删除 user_table 关联表记录
			int x = user_TableMapper.deleteUser_TableByUserIdAndTableId(table_id, user_id,Integer.parseInt(times));
			if(x == 0)
				throw new ServiceException("删除user_table表中记录失败");
			
			//2 删除input所有指定记录
			int y = inputMapper.deleteInputByTableIdAndUserId(table_id, user_id,Integer.parseInt(times));
			if(y == 0)
				throw new ServiceException("删除input表中记录失败");
			
			//如果上面都没有错，那么说明可以删除对应硬盘上的文件
			if(inputList != null && !inputList.isEmpty()){
				for (Input input : inputList) {
					AdminUtils.deleteFileByInput(input);
				}
			}
			
			
			
			trManager.commit(status);//完成提交
		} catch (ServiceException e) {
			logger.error(e.getMessage());
			trManager.rollback(status);
			throw new ServiceException("出现未知错误");
		} catch (Exception e) {
			logger.error(e.getMessage());
			trManager.rollback(status);
			throw new ServiceException("出现未知错误");
		}
		
		return true;
	}


	public int selectAllUser_TableByTableId(String table_id) throws ServiceException {
		
		int n = 0;
		try{
			n = user_TableMapper.selectAllUser_TableByTableId(table_id);
		}catch(Exception e){
			logger.error(e.getMessage());
			throw new ServiceException("查询指定table_id所有的user_table表出现未知异常");
		}
		return n;
	}


	public List<User_Table> getAllUser_Table(UserTableSearch userTableSearch,int num1,int num2) throws ServiceException {
		return user_TableMapper.getAllUser_Table(userTableSearch,num1, num2);
	}


	public int getUser_TableTotal(UserTableSearch userTableSearch) throws ServiceException {
		return user_TableMapper.getUser_TableTotal(userTableSearch);
	}

}






















