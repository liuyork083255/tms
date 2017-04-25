package cn.edu.sspu.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.sspu.exception.ServiceException;
import redis.clients.jedis.Jedis;

public class RedisUtils {
	private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
	
	private static Jedis redis = null;
	public static Jedis getRedisMaster(){
		String host = AdminUtils.getRedisHost("redis.master.host");
		int port = AdminUtils.getRedisIP("redis.master.port");
		redis =  new Jedis(host,port);
		return redis;
	}
	
	public static Jedis getRedisSlave1(){
		String host = AdminUtils.getRedisHost("redis.slave1.host");
		int port = AdminUtils.getRedisIP("redis.slave1.port");
		redis =  new Jedis(host,port);
		return redis;
	}
	
	public static Jedis getRedisSlave2(){
		String host = AdminUtils.getRedisHost("redis.slave2.host");
		int port = AdminUtils.getRedisIP("redis.slave2.port");
		redis =  new Jedis(host,port);
		return redis;
	}
	
	public static void setValue(String name,byte[] bytes){
		redis.set(name.getBytes(), bytes);
		int expireTime = AdminUtils.getRedisExpireTime("redis.master.expiresecond");
		redis.expire(name.getBytes(), expireTime);
	}
	
	public static byte[] getValue(String  name){
		return redis.get(name.getBytes());
	}
	
	
	public static boolean outFile(String userid,String inputid,String filetype) throws Exception{
		if(userid == null || inputid == null)
			new ServiceException("redis获取存入硬盘参数为空");
		
		try{
			RedisUtils.getRedisSlave1();
			byte[] bytes = getValue(inputid);
			
			if(bytes == null)
				throw new ServiceException("上传文件已过期，请重新上传");

			String filePath = AdminUtils.getFileUploadPath("fileUploadPath");
			File fileIO = new File(filePath + userid );
			if(!fileIO.exists()){
				boolean mkdirFlag = fileIO.mkdirs();
				if(!mkdirFlag)
					throw new ServiceException("redis创建文件路径失败");
			}
			DataOutputStream fw = new DataOutputStream(new FileOutputStream(fileIO+ "/" +inputid+ filetype));
			fw.write(bytes);
			fw.close();
		}catch(Exception e){
			try{
				RedisUtils.getRedisSlave2();
				byte[] bytes = getValue(inputid);
				
				if(bytes == null)
					throw new ServiceException("上传文件已过期，请重新上传");
				
				String filePath = AdminUtils.getFileUploadPath("fileUploadPath");
				File fileIO = new File(filePath + userid );
				if(!fileIO.exists()){
					boolean mkdirFlag = fileIO.mkdirs();
					if(!mkdirFlag)
						throw new ServiceException("redis创建文件路径失败");
				}
				DataOutputStream fw = new DataOutputStream(new FileOutputStream(fileIO+ "/" +inputid+ filetype));
				fw.write(bytes);
				fw.close();
			}catch(Exception e1){
				logger.error("redis 保存文件失败，异常信息 ： " + e1.getMessage());
				throw new ServiceException("redis 保存文件失败");
			}
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
}
