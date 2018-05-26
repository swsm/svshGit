package com.core.tools.file;

import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 
 * <p>ClassName: ExcelUtil</p>
 * <p>Description: excel报表导出</p>
 */
public class ExcelUtil {
    
    /**
     * <p>Description: 导出excel 只支持最多65535行数据</p>
     * @param response 响应
     * @param list 列表(里面是 实体类)
     * @param clazz 实体类
     * @param columnNames 表格列名 (注意与fileNames一 一对应)
     * @param fileNames 属性名称   (注意与columnNames一 一对应)
     * @param fileName 导出文件名称
     * @throws SecurityException 异常
     * @throws IllegalArgumentException 异常
     * @throws IllegalAccessException 异常
     * @throws InvocationTargetException 异常
     * @throws IOException 异常
     * @return String 导出的结果
     * @throws IOException  IO异常
     * @throws InstantiationException 创建新实例异常 
     */
    @SuppressWarnings("deprecation")
    public static String exportExcel(HttpServletResponse response, List<?> list, Class clazz, String[] columnNames,
        String[] fileNames, String fileName) throws SecurityException, IllegalArgumentException,
        IllegalAccessException, InvocationTargetException,
        IOException, InstantiationException {
        // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb;
        wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet;
        sheet = wb.createSheet("sheet1");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style;
        style = wb.createCellStyle();
        HSSFCellStyle styleColumnName;
        styleColumnName = wb.createCellStyle();
        HSSFFont font;
        font = wb.createFont();
        font.setFontName("仿宋_GB2312");
        //粗体显示
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体大小
        font.setFontHeightInPoints((short) 10);
        styleColumnName.setFont(font);
        // 创建一个居中格式
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);   
        //styleColumnName.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        // 去除数字科学计数法问题
        HSSFDataFormat format;
        format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        
        //设置边框
        styleColumnName.setBorderBottom((short) 1);  
        styleColumnName.setBorderTop((short) 1);
        styleColumnName.setBorderLeft((short) 1);
        styleColumnName.setBorderRight((short) 1);  
        
        style.setBorderBottom((short) 1);  
        style.setBorderTop((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);  
        
        HSSFCell cell;
        for (int i = 0; i < columnNames.length; i++) {
            cell = row.createCell((short) i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(styleColumnName);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow((int) i + 1);
            Object o = clazz.newInstance();
            o = list.get(i);  
            // 第四步，创建单元格，并设置值
            for (int j = 0; j < fileNames.length; j++) {
                String methodName;
                methodName = "get" + fileNames[j].substring(0, 1).toUpperCase() 
                        + fileNames[j].substring(1, fileNames[j].length());
                Method[] ms;
                ms = clazz.getMethods();
                for (Method m : ms) {
                    if (m.getName().equals(methodName)) {
                        cell = row.createCell((short) j);
                        if (m.invoke(o) instanceof Double) {
                            String str = "";
                            DecimalFormat decimalFormat;
                            decimalFormat = new DecimalFormat("#############.####");
                            str = str + decimalFormat.format(m.invoke(o));
                            cell.setCellValue("null".equals(String.valueOf(m.invoke(o))) ?
                                    "" : str);
                        } else if (m.invoke(o) instanceof Date) {
                            String str = "";
                            SimpleDateFormat dateFormat;
                            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            str = str + dateFormat.format(m.invoke(o));
                            cell.setCellValue("null".equals(String.valueOf(m.invoke(o))) ?
                                    "" : str);
                        } else {
                            cell.setCellValue("null".equals(String.valueOf(m.invoke(o))) ?
                                "" : String.valueOf(m.invoke(o)));
                        }
                        cell.setCellStyle(style);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        break;
                    }
                }
            }
        }  
        
        OutputStream out = null;                                                  
        try {                                                                     
            response.reset();                                                     
            out = response.getOutputStream();                                     
            fileName = fileName + ".xls";                                   
            // 设定输出文件头                                                            
            response.setContentType("application/x-msdownload");                  
            response.setHeader("Content-Disposition", "attachment;filename="      
                    + new String(fileName.getBytes("utf-8"), "iso8859-1"));   
            wb.write(out); 
        }  finally {                                                               
            out.close();
        }
        return "success";
    }
    
    /**
     * <p>Description: 导出excel Map 形式 只支持最多65535行数据</p>
     * @param response 响应
     * @param list 列表(里面是 实体类)
     * @param columnNames 表格列名 (注意与fileNames一 一对应)
     * @param fileNames 属性名称   (注意与columnNames一 一对应)
     * @param fileName 导出文件名称
     * @throws SecurityException 异常
     * @throws IllegalArgumentException 异常
     * @throws IllegalAccessException 异常
     * @throws InvocationTargetException 异常
     * @throws IOException 异常
     * @return String 导出的结果
     * @throws IOException  IO异常
     * @throws InstantiationException 创建新实例异常 
     */
    @SuppressWarnings("deprecation")
    public static String exportExcel(HttpServletResponse response, List<Map<String, Object>> list, String[] columnNames,
        String[] fileNames, String fileName) throws SecurityException, IllegalArgumentException,
        IllegalAccessException, InvocationTargetException,
        IOException, InstantiationException {
        // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb;
        wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet;
        sheet = wb.createSheet("sheet1");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style;
        style = wb.createCellStyle();
        HSSFCellStyle styleColumnName;
        styleColumnName = wb.createCellStyle();
        HSSFFont font;
        font = wb.createFont();
        font.setFontName("仿宋_GB2312");
        //粗体显示
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体大小
        font.setFontHeightInPoints((short) 10);
        styleColumnName.setFont(font);
        // 创建一个居中格式
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);   
        //styleColumnName.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        // 去除数字科学计数法问题
        HSSFDataFormat format;
        format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        
        //设置边框
        styleColumnName.setBorderBottom((short) 1);  
        styleColumnName.setBorderTop((short) 1);
        styleColumnName.setBorderLeft((short) 1);
        styleColumnName.setBorderRight((short) 1);  
        
        style.setBorderBottom((short) 1);  
        style.setBorderTop((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);  
        
        HSSFCell cell;
        for (int i = 0; i < columnNames.length; i++) {
            cell = row.createCell((short) i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(styleColumnName);
        }
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow((int) i + 1);
            Map<String, Object> 
            map = list.get(i);  
            // 第四步，创建单元格，并设置值
            for (int j = 0; j < fileNames.length; j++) {
                cell = row.createCell((short) j);
                cell.setCellValue(map.get(fileNames[j]) == null ? "" : String.valueOf(map.get(fileNames[j])));
                cell.setCellStyle(style);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            }
        }  
        
        OutputStream out = null;                                                  
        try {                                                                     
            response.reset();                                                     
            out = response.getOutputStream();                                     
            fileName = fileName + ".xls";                                   
            // 设定输出文件头                                                            
            response.setContentType("application/x-msdownload");                  
            response.setHeader("Content-Disposition", "attachment;filename="      
                    + new String(fileName.getBytes("utf-8"), "iso8859-1"));   
            wb.write(out); 
        }  finally {                                                               
            out.close();
        }
        return "success";
    }
    
}
