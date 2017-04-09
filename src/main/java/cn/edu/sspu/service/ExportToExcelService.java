package cn.edu.sspu.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.User_Table;

public interface ExportToExcelService {
	public boolean exportToExcel(List<User_Table> user_tableList,String tableName,HttpServletResponse response) throws ServiceException;

}
