package cn.edu.sspu.service.serviceImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

	public List<File> getAllFile(int page,int rows) {
		
		return fileMapper.getAllFile(page, rows);
	}

	public int selectFileTotal() {
		return fileMapper.selectAllFileTotal();
	}

	public boolean onloadFileByInputId(String input_id, String user_id, String type, HttpServletResponse response) {
		String path = AdminUtils.getFileUploadPath("fileUploadPath");//获取文件保存的绝对路径
		path = path+user_id+"\\"+input_id+"."+type;
		//获取文件名称
		Input input = inputMapper.selectInputById(input_id);
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
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
}





















