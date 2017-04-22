package cn.edu.sspu.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.sspu.models.File;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.searchfilter.UserTableSearch;

public interface FileMapper {
	public int insertFile(File file);
	
	public int updateFile(File file);
	
	public int deleteFileByInputId(String input_id);
	
	
	//下面两个是获取全部的file
	public int selectAllFileTotal();
	
	public List<File> getAllFile(@Param("num1")int num1,@Param("num2") int num2);
	
	//下面两个是获取指定用户的全部file
	public List<File> getAllFileByUserId(@Param("num1")int page,@Param("num2")int rows,@Param("user_id")String user_id);
	
	public int selectFileTotalByUserId(String user_id); 
	
	
	public File selectFileById(@Param("input_id")String input_id);

}
