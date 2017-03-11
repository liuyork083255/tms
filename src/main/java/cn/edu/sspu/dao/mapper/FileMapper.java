package cn.edu.sspu.dao.mapper;

import cn.edu.sspu.models.File;
import cn.edu.sspu.models.Input;

public interface FileMapper {
	public int insertFile(File file);
	
	public int updateFile(File file);
	
	public int deleteFileByInputId(String input_id);

}
