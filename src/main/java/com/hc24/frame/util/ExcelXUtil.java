package com.hc24.frame.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * 新版Excel工具，可支持.xls和.xlsx两种格式 <br/>
 * 采用POI实现
 * 
 * @author hc24
 * 
 */
public class ExcelXUtil {
	public static final String FILE_TYPE_XLS = "xls";
	public static final String FILE_TYPE_XLSX = "xlsx";

	public static InputStream exportExcel(String fileType, String title,
			String[] headers, Object[][] datas) {
		Workbook wb = null;
		if (FILE_TYPE_XLS.equals(fileType)) {
			wb = new HSSFWorkbook();
		} else if (FILE_TYPE_XLSX.equals(fileType)) {
			wb = new XSSFWorkbook();
		} else {
			throw new RuntimeException("文件格式不正确");
		}

		if (headers != null && headers.length > 0) {
			if (datas != null && datas.length > 0) {
				if (datas[0].length != headers.length) {
					throw new RuntimeException("表头与数据列数不一致");
				}
			}
		}
		
		if(title==null||"".equals(title)){
			title="sheet1";
		}

		// 创建sheet对象
		Sheet sheet = wb.createSheet(title);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			// 写入表头
			if (headers != null && headers.length > 0) {
				Row row = sheet.createRow(0);
				for (int i = 0; i < headers.length; i++) {
					Cell cell = row.createCell(i);
					cell.setCellValue(headers[i]);
				}
			}

			// 写入数据
			if (datas != null && datas.length > 0) {
				for (int i = 0; i < datas.length; i++) {
					Row row = sheet.createRow(i + 1);
					for (int j = 0; j < datas[i].length; j++) {
						Cell cell = row.createCell(j);
						setCellValue(cell, datas[i][j]);
					}
				}
			}

			
			wb.write(os);
			byte[] ba=os.toByteArray();
			os.close();
			return new ByteArrayInputStream(ba);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("导出Excel数据出错");
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static List<Object[]> importExcel(String fileType,InputStream is){
		List<Object[]> list=new ArrayList<Object[]>();
		Workbook wb = null;
		try{
			if (FILE_TYPE_XLS.equals(fileType)) {
				wb = new HSSFWorkbook(is);
			} else if (FILE_TYPE_XLSX.equals(fileType)) { 
				wb = new XSSFWorkbook(is);
			}else{
				throw new RuntimeException("文件格式不正确");
			}
			//读取第一个
			Sheet sheet= wb.getSheetAt(0);
		
			for(int i=sheet.getFirstRowNum(); i<=sheet.getLastRowNum(); i++){
				Row row = sheet.getRow(i);
			//for(Row row:sheet){
				Object[] data = new Object[row.getLastCellNum()]; 
				for(int j=row.getFirstCellNum(); j<row.getLastCellNum(); j++){
					Cell cell=row.getCell(j);
					
				//for(Cell cell:row){
					String str = "";
					if(cell != null) 
						str=getCellFormatValue(cell);/* 这里全部转化成字符串 */
					//String str=getCellFormatValue(cell);/* 这里全部转化成字符串了 */
					data[j]=str;
				}
				list.add(data);
			}
			return list;
		}catch(Exception e){
			throw new RuntimeException("读取Excel： 出错",e);
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据excel路径读取文件内容<br/>
	 * 支持xls和xlsx
	 * @param filePath 路径
	 * @return
	 */
	public static List<Object[]> importExcel(String filePath){
		if(filePath==null||"".equals(filePath))
			throw new RuntimeException("filePath is null");
		String fileType=filePath.substring(filePath.lastIndexOf(".")+1);
		FileInputStream inputStream=null;
		try {
			inputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("读取Excel："+filePath+" 出错,文件不存在",e);
		}
		return importExcel(fileType, inputStream);
		
	
	}
	
	/**
	 * 根据data数据类型，设置单元格的数据格式
	 * @param cell 单元格
	 * @param data 数据
	 */
	private static void setCellValue(Cell cell,Object data){
		//cell.setCellValue(data.toString());
		//整数
		if(data instanceof Short
				||data instanceof Integer
				||data instanceof Long){
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			CellStyle cs=cell.getCellStyle();
			cs.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
			cell.setCellStyle(cs);
			double d=Double.parseDouble(data.toString());
			cell.setCellValue(d);
			//小数，对小数位数需要进行动态判断了
		}else if(data instanceof Float
				||data instanceof Double){
			String str=data.toString();
			int bitPos=str.indexOf(".");
			String fm="0"+str.substring(bitPos);/* 类似于0.00这种样子 */
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			CellStyle cs=cell.getCellStyle();
			cs.setDataFormat(HSSFDataFormat.getBuiltinFormat(fm));
			cell.setCellStyle(cs);
			double d=Double.parseDouble(data.toString());
			cell.setCellValue(d);
		}else{
			cell.setCellValue(data.toString());
		}
		
		
		
		
		
		
	}
	
	 /**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private static String getStringCellValue(Cell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            strCell = cell.getStringCellValue();
            break;
        case Cell.CELL_TYPE_NUMERIC:
            strCell = String.valueOf(cell.getNumericCellValue());
            break;
        case Cell.CELL_TYPE_BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_BLANK:
            strCell = "";
            break;
        default:
            strCell = "";
            break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     * 
     * @param cell
     *            Excel单元格
     * @return String 单元格数据内容
     */
    private static String getDateCellValue(Cell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == Cell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                        + "-" + date.getDate();
            } else if (cellType == Cell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }
	
    
	/**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case Cell.CELL_TYPE_NUMERIC:
            case Cell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
	
	public static void main(String[] args) throws Exception {
		//写数据
		String[] headers=new String[]{"序号","分数","数据","备注"};
		Object[][] datas=new Object[][]{{1,78.5f,"huangchao","remark"},{2,86.8,"dongfang","backup"}};
		
		InputStream is= ExcelXUtil.exportExcel(ExcelXUtil.FILE_TYPE_XLSX, "test data", headers, datas);
		
		System.out.println("写入文件...");
		
		FileOutputStream fos=new FileOutputStream("d:/desktop/text.xlsx");
		
		byte[] buffer=new byte[1024*2];
		while(is.read(buffer)>0){
			fos.write(buffer);
		}
		
		fos.close();
		is.close();
		
		//读数据
//		System.out.println("读取文件...");
//		List<Object[]> list=ExcelXUtil.importExcel("x:/白名单.xls");
//		for(Object[] data:list){
//			System.out.println();
//			for(Object o:data){
//				System.out.print(o+" , ");
//			}
//		}
	}

}
