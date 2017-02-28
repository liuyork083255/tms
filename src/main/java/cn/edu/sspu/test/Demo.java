package cn.edu.sspu.test;

import java.util.UUID;

public class Demo {
	
	public static void  main(String[] args){
		System.out.println(UUID.randomUUID().toString().replace("-", "").toLowerCase());
	}
	
	
}
