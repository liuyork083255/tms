package cn.edu.sspu.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.edu.sspu.exception.ServiceException;
import cn.edu.sspu.models.Input;
import cn.edu.sspu.models.Table;
import cn.edu.sspu.models.User_Table;


public class ExcelUtils {
	 public static String NO_DEFINE = "no_define";//未定义的字段
	    public static String DEFAULT_DATE_PATTERN="yyyy年MM月dd日";//默认日期格式
	    public static int DEFAULT_COLOUMN_WIDTH = 17;
	    /**
	     * 导出Excel 97(.xls)格式 ，少量数据
	     * @param title 标题行 
	     * @param headMap 属性-列名
	     * @param jsonArray 数据集
	     * @param datePattern 日期格式，null则用默认日期格式
	     * @param colWidth 列宽 默认 至少17个字节
	     * @param out 输出流
	     */
	    public static void exportExcel(String title,Map<String, String> headMap,JSONArray jsonArray,String datePattern,int colWidth, OutputStream out) {
	        if(datePattern==null) datePattern = DEFAULT_DATE_PATTERN;
	        // 声明一个工作薄
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        workbook.createInformationProperties();
	        workbook.getDocumentSummaryInformation().setCompany("*****公司");
	        SummaryInformation si = workbook.getSummaryInformation();
	        si.setAuthor("JACK");  //填加xls文件作者信息
	        si.setApplicationName("导出程序"); //填加xls文件创建程序信息
	        si.setLastAuthor("最后保存者信息"); //填加xls文件最后保存者信息
	        si.setComments("JACK is a programmer!"); //填加xls文件作者信息
	        si.setTitle("POI导出Excel"); //填加xls文件标题信息
	        si.setSubject("POI导出Excel");//填加文件主题信息
	        si.setCreateDateTime(new Date());
	         //表头样式
	        HSSFCellStyle titleStyle = workbook.createCellStyle();
	        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont titleFont = workbook.createFont();
	        titleFont.setFontHeightInPoints((short) 20);
	        titleFont.setBoldweight((short) 700);
	        titleStyle.setFont(titleFont);
	        // 列头样式
	        HSSFCellStyle headerStyle = workbook.createCellStyle();
	        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont headerFont = workbook.createFont();
	        headerFont.setFontHeightInPoints((short) 12);
	        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        headerStyle.setFont(headerFont);
	        // 单元格样式
	        HSSFCellStyle cellStyle = workbook.createCellStyle();
	        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	        HSSFFont cellFont = workbook.createFont();
	        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	        cellStyle.setFont(cellFont);
	        // 生成一个(带标题)表格
	        HSSFSheet sheet = workbook.createSheet();
	        // 声明一个画图的顶级管理器
	        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	        // 定义注释的大小和位置,详见文档
	        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
	                0, 0, 0, (short) 4, 2, (short) 6, 5));
	        // 设置注释内容
	        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
	        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	        comment.setAuthor("JACK");
	        //设置列宽
	        int minBytes = colWidth<DEFAULT_COLOUMN_WIDTH?DEFAULT_COLOUMN_WIDTH:colWidth;//至少字节数
	        int[] arrColWidth = new int[headMap.size()];
	        // 产生表格标题行,以及设置列宽
	        String[] properties = new String[headMap.size()];
	        String[] headers = new String[headMap.size()];
	        int ii = 0;
	        for (Iterator<String> iter = headMap.keySet().iterator(); iter
	                .hasNext();) {
	            String fieldName = iter.next();

	            properties[ii] = fieldName;
	            headers[ii] = fieldName;

	            int bytes = fieldName.getBytes().length;
	            arrColWidth[ii] =  bytes < minBytes ? minBytes : bytes;
	            sheet.setColumnWidth(ii,arrColWidth[ii]*256);
	            ii++;
	        }
	        // 遍历集合数据，产生数据行
	        int rowIndex = 0;
	        for (Object obj : jsonArray) {
	            if(rowIndex == 65535 || rowIndex == 0){
	                if ( rowIndex != 0 ) sheet = workbook.createSheet();//如果数据超过了，则在第二页显示

	                HSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
	                titleRow.createCell(0).setCellValue(title);
	                titleRow.getCell(0).setCellStyle(titleStyle);
	                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

	                HSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
	                for(int i=0;i<headers.length;i++)
	                {
	                    headerRow.createCell(i).setCellValue(headers[i]);
	                    headerRow.getCell(i).setCellStyle(headerStyle);

	                }
	                rowIndex = 2;//数据内容从 rowIndex=2开始
	            }
	            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
	            HSSFRow dataRow = sheet.createRow(rowIndex);
	            for (int i = 0; i < properties.length; i++)
	            {
	                HSSFCell newCell = dataRow.createCell(i);

	                Object o =  jo.get(properties[i]);
	                String cellValue = ""; 
	                if(o==null) cellValue = "";
	                else if(o instanceof Date) cellValue = new SimpleDateFormat(datePattern).format(o);
	                else cellValue = o.toString();

	                newCell.setCellValue(cellValue);
	                newCell.setCellStyle(cellStyle);
	            }
	            rowIndex++;
	        }
	        // 自动调整宽度
	        /*for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }*/
	        try {
	            workbook.write(out);
	            workbook.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    

	    public static void main(String[] args) throws IOException {
	        /*int count = 100000;
	        JSONArray ja = new JSONArray();
	        for(int i=0;i<100;i++){
	            Student s = new Student();
	            s.setName("POI"+i);
	            s.setAge(i);
	            s.setBirthday(new Date());
	            s.setHeight(i);
	            s.setWeight(i);
	            s.setSex(i/2==0?false:true);
	            ja.add(s);
	        }
	        Map<String,String> headMap = new LinkedHashMap<String,String>();
	        headMap.put("name","姓名");
	        headMap.put("age","年龄");
	        headMap.put("birthday","生日");
	        headMap.put("height","身高");
	        headMap.put("weight","体重");
	        headMap.put("sex","性别");

	        String title = "测试";
	        
	        OutputStream outXls = new FileOutputStream("E://a.xls");
	        System.out.println("正在导出xls....");
	        Date d = new Date();
	        ExcelUtils.exportExcel(title,headMap,ja,null,0,outXls);
	        System.out.println("共"+count+"条数据,执行"+(new Date().getTime()-d.getTime())+"ms");
	        outXls.close();*/
	        //
	    }
	    
	    
	    
	    public static boolean exceportToExcelType_1(List<User_Table> user_tableList,Map<String,
	    		List<Input>> inputListMap,List<Input> tableModel,String tableName) throws ServiceException{
	    	// 1 首先校验参数了
	    	
	    	// 2 封装导出类
	        // 第一步，创建一个webbook，对应一个Excel文件  
	        HSSFWorkbook wb = new HSSFWorkbook();  
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("sheet-1");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        HSSFRow row = sheet.createRow((int) 0);  
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	    	
	        
	        
	        //设置每一列的名称
	        HSSFCell cell = null; 
	        int shotNum = 0;
        	cell = row.createCell(shotNum++); 
	        cell.setCellValue("姓名");
	        cell.setCellStyle(style); 
	        for (Input input : tableModel) {
	        	cell = row.createCell(shotNum++); 
		        cell.setCellValue(input.getName());
		        cell.setCellStyle(style); 
			}
	        
	        //遍历map，封装到表中
	        int shortGET = 0;
	        int userId = 0;
	        Set<String> keys =  inputListMap.keySet();
			for (String username : keys) {
				int num = 0;
				row = sheet.createRow(++shortGET);
				List<Input> inputList = inputListMap.get(username);
				row.createCell(num++).setCellValue(user_tableList.get(userId++).getUser_name()); //这里是写入新增的名字列
				for (Input input : inputList) {
					row.createCell(num++).setCellValue(input.getValue());  //这里是名字后面的列
				}
			}
			
	        //将文件存到指定位置  
			String excelExportPath = AdminUtils.getExcelExportPathPath("excelExportPath");
	        try  
	        {  
	            FileOutputStream fout = new FileOutputStream(excelExportPath + tableName + ".xls");  
	            wb.write(fout);  
	            fout.close();  
	        }  
	        catch (Exception e)  
	        {  
	        	e.printStackTrace();
	        	if(e instanceof FileNotFoundException)
	        		throw new ServiceException("该文件名被未知程序使用，不可导出");
	            throw new ServiceException("导出失败，异常未知");
	        } 
			
	    	return true;
	    }
	    
	    public static boolean exceportToExcelType_2(List<User_Table> user_tableList,Map<String,
	    		List<Input>> inputListMap,List<Input> tableModel,String tableName,HttpServletResponse response) throws ServiceException{
	    	// 1 首先校验参数了
	    	
	    	// 2 封装导出类
	        // 第一步，创建一个webbook，对应一个Excel文件  
	        HSSFWorkbook wb = new HSSFWorkbook();  
	        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	        HSSFSheet sheet = wb.createSheet("sheet-1");  
	        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
	        HSSFRow row = sheet.createRow((int) 0);  
	        // 第四步，创建单元格，并设置值表头 设置表头居中  
	        HSSFCellStyle style = wb.createCellStyle();  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	    	
	        
	        
	        //设置每一列的名称
	        HSSFCell cell = null; 
	        int shotNum = 0;
        	cell = row.createCell(shotNum++); 
	        cell.setCellValue("姓名");
	        cell.setCellStyle(style); 
	        for (Input input : tableModel) {
	        	cell = row.createCell(shotNum++); 
		        cell.setCellValue(input.getName());
		        cell.setCellStyle(style); 
			}
	        
	        //遍历map，封装到表中
	        int shortGET = 0;
	        int userId = 0;
	        Set<String> keys =  inputListMap.keySet();
			for (String username : keys) {
				int num = 0;
				row = sheet.createRow(++shortGET);
				List<Input> inputList = inputListMap.get(username);
				row.createCell(num++).setCellValue(user_tableList.get(userId++).getUser_name()); //这里是写入新增的名字列
				for (Input input : inputList) {
					row.createCell(num++).setCellValue(input.getValue());  //这里是名字后面的列
				}
			}
			
	        //将文件存到指定位置  
	        try  
	        {  
	            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
	            response.setHeader("Content-Disposition","attachment");
	            response.setHeader("filename",tableName+".xls");
				OutputStream os = new BufferedOutputStream(response.getOutputStream());
	            wb.write(os);
			    os.flush();
			    os.close(); 
	        }  
	        catch (Exception e){  
	        	e.printStackTrace();
	        	return false;
	        } 
			
	    	return true;
	    }
	    
}





























