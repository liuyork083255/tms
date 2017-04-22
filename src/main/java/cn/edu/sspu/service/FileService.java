package cn.edu.sspu.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.edu.sspu.models.File;

public interface FileService {
	public List<File> getAllFile(int page,int rows);
	
	public int selectFileTotal(); 
	
	public List<File> getAllFileByUserId(int page,int rows,String user_id);
	
	public int selectFileTotalByUserId(String user_id); 
	
	public boolean onloadFileByInputId(String input_id,String user_id,String type,HttpServletResponse response);
	
	public boolean validateFilePath(String input_id);
}
