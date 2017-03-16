package cn.edu.sspu.service;

import java.util.List;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.User_Table;

public interface ExportToExcelService {
	public boolean exportToExcel(List<User_Table> user_tableList) throws ServiceException;

}
