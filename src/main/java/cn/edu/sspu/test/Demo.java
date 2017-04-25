package cn.edu.sspu.test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import redis.clients.jedis.Jedis;

public class Demo {
	
	public static void main(String[] args) {
		
		java.io.File file = new java.io.File("F:\\a.*");
		file.delete();
		
		
		
		
	}
	
	public static void set(){
	    Jedis redis = new Jedis("120.25.251.210");
	    File file = new File("C:\\Users\\liu.yang\\Desktop\\产品方案完整版.rp");
	    if(!file.exists())
	        return ;
	    byte[] fileBytes = getFileToByte(file);
	    redis.set("test1".getBytes(),fileBytes);
	}
	public static void get(){
		Jedis redis = new Jedis("120.25.251.210");
	    byte[] bytes = redis.get("test1".getBytes());
	    File outfile = new File("F:\\aaa.rp");
	    try {
	        DataOutputStream fw = new DataOutputStream(new FileOutputStream(outfile));
	        fw.write(bytes);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static byte[] getFileToByte(File file) {
	    byte[] by = new byte[(int) file.length()];
	    try {
	        InputStream is = new FileInputStream(file);
	        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
	        byte[] bb = new byte[2048];
	        int ch;
	        ch = is.read(bb);
	        while (ch != -1) {
	            bytestream.write(bb, 0, ch);
	            ch = is.read(bb);
	        }
	        by = bytestream.toByteArray();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return by;
	}
	
	
}
