package com.hc24.frame.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import jxl.Workbook;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet; 
import jxl.write.WritableWorkbook;

/**
 * 
 * Excel Tools 只适用用导出单个表格
 * <br/> 
 * 采用jxl，只能导出.xls文件格式，.xlsx文件格式请采用POI {@link ExcelXUtil}
 * 
 * @author hc24
 * 
 */
@Deprecated
public class ExcelUtil {
	
	/**
	 * 对于WritableCellFormat实例的数量jxl是有限制的，所以把这些用到的实例拿出来。
	 */
	private static WritableCellFormat intWcf= new WritableCellFormat(NumberFormats.INTEGER);	//整数格式
	private static WritableCellFormat doubleWcf = new WritableCellFormat(new NumberFormat("0.0"));//浮点数格式，保留一位小数
		
	/**
	 * @param title
	 *            标题
	 * @param headers
	 *            表头
	 * @param columnView
	 *            列宽
	 * @param datas
	 *            数据
	 * @throws Exception
	 */
	public static InputStream exportExcel(String title, List<String> headers, List<Integer> columnView, List<Object[]> datas) throws Exception {
		if (headers == null || headers.size() == 0)
			throw new Exception("没有设置表头");
		if (datas == null || datas.size() == 0) {
			throw new Exception("没有数据，无法导出Excel");
		}
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		// 行号
		int rowNum = 0;
		// 列号
		int colNum = 0;
		// 建立工作文件
		WritableWorkbook wwb = null;
		try {
			wwb = Workbook.createWorkbook(os);
			// 创建Excel工作表,工作表名为title
			WritableSheet ws = wwb.createSheet("sheet 1", 0);
			// 设置标题
			if (title != null && !"".equals(title)) {
				ws.mergeCells(0, 0, headers.size() - 1, 0);
				writeContentToCell(colNum, rowNum, title, ws);
				rowNum++;
			}
			// 设置表头
			for (int i = 0; i < headers.size(); i++) {
				jxl.write.Label label = new jxl.write.Label(i, rowNum, headers.get(i));
				WritableCellFormat cf = new WritableCellFormat();
				cf.setBackground(jxl.format.Colour.GRAY_25);
				label.setCellFormat(cf);
				ws.setColumnView(i, columnView.get(i));
				ws.addCell(label);
			}
			rowNum++;
			// 写入数据
			for (int i = 0; i < datas.size(); i++) {
				Object[] objs = datas.get(i);
				for (int j = 0; j < objs.length; j++) {
					if(objs[j]==null){
						writeContentToCell(j, rowNum, "", ws);
						continue;
					}
					if(objs[j] instanceof String)
						writeContentToCell(j, rowNum, objs[j].toString(), ws);
					else if(objs[j] instanceof Integer)
						writeIntContentToCell(j, rowNum, objs[j].toString(), ws);
					else
						writeDoubleContentToCell(j, rowNum, objs[j].toString(), ws);
				}
				rowNum++;
			}
			// 写入流
			wwb.write();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("导出Excel出错！");
		} finally {
			// 关闭流
			if (wwb != null)
				wwb.close();
		}
		byte[] ba=os.toByteArray();
		os.close();
		return new ByteArrayInputStream(ba);
	}
	
	/**
	 * 将指定内容保存到单元格,格式为默认
	 * 
	 * @param row
	 * @param col
	 * @param content
	 * @param writablesheet
	 * @throws Exception
	 */
	private static void writeContentToCell(int col, int row, String content, WritableSheet writablesheet) throws Exception {
		jxl.write.Label label = new jxl.write.Label(col, row, content);
		writablesheet.addCell(label);
	}
	
	/**
	 * 将指定内容保存到单元格,格式为整数
	 * 
	 * @param row
	 * @param col
	 * @param content
	 * @param writablesheet
	 * @throws Exception
	 */
	private static void writeIntContentToCell(int col, int row, String content, WritableSheet writablesheet) throws Exception {
		Number label = new Number(col, row, Double.parseDouble(content), intWcf);
		writablesheet.addCell(label);
	}
	
	/**
	 * 将指定内容保存到单元格,格式为浮点数
	 * 
	 * @param row
	 * @param col
	 * @param content
	 * @param writablesheet
	 * @throws Exception
	 */
	private static void writeDoubleContentToCell(int col, int row, String content, WritableSheet writablesheet) throws Exception {
		Number label = new Number(col, row, Double.parseDouble(content), doubleWcf);
		writablesheet.addCell(label);
	}
	
}
