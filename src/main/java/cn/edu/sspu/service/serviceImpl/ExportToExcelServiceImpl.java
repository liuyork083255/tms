package cn.edu.sspu.service.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.fastjson.JSON;

import cn.edu.sspu.dao.mapper.InputMapper;
import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.User_Table;
import cn.edu.sspu.service.ExportToExcelService;
import cn.edu.sspu.utils.ExcelUtils;

@Service
public class ExportToExcelServiceImpl implements ExportToExcelService {
	private static Logger logger = LoggerFactory.getLogger(ExportToExcelServiceImpl.class);
	@Autowired
	private InputMapper inputMapper;

	public boolean exportToExcel(List<User_Table> user_tableList,String tableName,HttpServletResponse response) throws ServiceException {
		// 1 首先是根据集合查询到所有的user填写的表单
		Map<String,List<Input>> map = new HashMap<String,List<Input>>();
		
		for (User_Table user_table : user_tableList) {
			List<Input> inputList = inputMapper.selectInputByUserIdAndTableIdAndTimes(user_table.getTable_id(), user_table.getUser_id(), user_table.getTimes());
			if(inputList != null)
				map.put(user_table.getUser_name() + "-" + user_table.getTimes(), inputList);
		}
		System.out.println(JSON.toJSONString(map, true));
		List<Input> tableModel = inputMapper.selectInputByTableId(user_tableList.get(0).getTable_id());
		if(tableModel == null)
			throw new ServiceException("获取table模板失败");
		
		//boolean flag = ExcelUtils.exceportToExcelType_1(user_tableList, map, tableModel,tableName);
		boolean flag1 = ExcelUtils.exceportToExcelType_3(user_tableList, map, tableModel,tableName,response);
		if(!flag1){
			return false;
		}
		//if(!flag)
		//	throw new ServiceException("Service 导出失败，异常未知");
		
		return true;
	}

}
