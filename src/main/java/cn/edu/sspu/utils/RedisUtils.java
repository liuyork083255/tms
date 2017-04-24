package cn.edu.sspu.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

public class RedisUtils {
	private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
	
	private static Jedis redis = null;
	public static Jedis getRedisCli(){
		String host = AdminUtils.getRedisHost("redis.master.HOST");
		int port = AdminUtils.getRedisIP("redis.master.PORT");
		redis =  new Jedis(host,port);
		return redis;
	}
	
	public static void setValue(String name,byte[] bytes){
		redis.set(name.getBytes(), bytes);
		int expireTime = AdminUtils.getRedisIP("redis.master.Expire");
		redis.expire(name.getBytes(), expireTime);
	}
	
	public static byte[] getValue(String  name){
		return redis.get(name.getBytes());
	}
	
	
	public static boolean outFile(byte[] bytes){
		String path = AdminUtils.getFileUploadPath("fileUploadPath");
		File outfile = new File(path);
		try {
			DataOutputStream fw = new DataOutputStream(new FileOutputStream(outfile));
			fw.write(bytes);
			fw.close();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
}
