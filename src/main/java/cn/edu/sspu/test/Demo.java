package cn.edu.sspu.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Demo {
	
	public static void  main(String[] args){
		long m = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = simpleDateFormat.format(new Date());
		System.out.println(time);
	}
	
	
}
