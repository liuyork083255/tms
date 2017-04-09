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
	
	public int selectAllFileTotal();
	
	public List<File> getAllFile(@Param("num1")int num1,@Param("num2") int num2);

}
