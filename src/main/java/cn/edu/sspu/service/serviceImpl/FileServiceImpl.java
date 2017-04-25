package cn.edu.sspu.service.serviceImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.sspu.dao.mapper.FileMapper;
import cn.edu.sspu.dao.mapper.InputMapper;
import cn.edu.sspu.models.File;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.service.FileService;
import cn.edu.sspu.utils.AdminUtils;

@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private InputMapper inputMapper;
	@Autowired
	private FileMapper fileMapper;
	private static Logger logger = LoggerFactory.getLogger(ExportToExcelServiceImpl.class);
	

	public List<File> getAllFile(int page,int rows) {
		
		return fileMapper.getAllFile(page, rows);
	}

	public int selectFileTotal() {
		return fileMapper.selectAllFileTotal();
	}

	public boolean onloadFileByInputId(String input_id, String user_id, String type, HttpServletResponse response) {
		String path = AdminUtils.getFileUploadPath("fileUploadPath");//获取文件保存的绝对路径
		
		logger.info("path1 : " + path);
		
		path = path+user_id+"/"+input_id+"."+type;
		//获取文件名称
		
		logger.info("path1 : " + path);
		
		Input input = inputMapper.selectInputById(input_id);
		
		logger.info("input : " + input);
		
		if(input == null)
			return false;
		try {
			java.io.File file = new java.io.File(path);
			InputStream in = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			in.close();
			
			response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(input.getValue(), "UTF-8"));
		    response.addHeader("Content-Length", "" + file.length());
			
		    OutputStream os = new BufferedOutputStream(response.getOutputStream());
		    response.setContentType("application/octet-stream");
		    os.write(buffer);// 输出文件
		    os.flush();
		    os.close();
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean validateFilePath(String input_id) {
		File file = fileMapper.selectFileById(input_id);
		if(file == null)
			return false;
		String path = AdminUtils.getFileUploadPath("fileUploadPath");//获取文件保存的绝对路径
		path = path+file.getUser_id()+"/"+input_id+file.getFiletype();
		java.io.File filePath = new java.io.File(path);
		if(!filePath.exists())
			return false;
		return true;
	}

	public List<File> getAllFileByUserId(int page, int rows, String user_id) {
		return fileMapper.getAllFileByUserId(page, rows, user_id);
	}

	public int selectFileTotalByUserId(String user_id) {
		return fileMapper.selectFileTotalByUserId(user_id);
	}
}





















