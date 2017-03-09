package cn.edu.sspu.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class Demo {
	
	public static void  main(String[] args) throws FileNotFoundException, IOException{
		Properties pps = new Properties();
		InputStream p1 = Demo.class.getClassLoader().getResourceAsStream("db.properties");
		pps.load(p1);
		String path = (String)pps.get("fileUploadPath");
		System.out.println(path);
	}
	
	
}
