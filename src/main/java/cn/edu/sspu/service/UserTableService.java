package cn.edu.sspu.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Model;
import cn.edu.sspu.models.Table;

public interface UserTableService {
	public List<Table> selectTableUnwriteByUserId(String user_id,int num1,int num2) throws ServiceException;
	
	public int selectTableUnwriteTotal();
	
	public boolean saveUserEditModel(Model model,HttpSession session) throws ServiceException;
}
