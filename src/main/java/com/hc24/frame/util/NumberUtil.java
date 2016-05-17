package com.hc24.frame.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数字格式化工
 * 
 * @author hc24
 */
public class NumberUtil {

	/**
	 * 格式化工具集
	 */
	private static final Map<String, NumberFormat> fmtMap = new HashMap<String, NumberFormat>();

	/**
	 * @param ex
	 *            格式表达
	 * @return 数字格式化工
	 */
	private static NumberFormat getFormat(String ex) {
		NumberFormat fmt = fmtMap.get(ex);
		if (fmt == null) {
			fmt = new DecimalFormat(ex);
			fmtMap.put(ex, fmt);
		}
		return fmt;
	}

	/**
	 * 格式 @param obj 对象
	 * 
	 * @param ex
	 *            格式表达 
	 *@return 数字字符
	 */
	public static synchronized String format(Object obj, String ex) {
		return getFormat(ex).format(obj);
	}

	/**
	 * 格式 @param number 数字
	 * 
	 * @param ex
	 *            格式表达 
	 * @return 数字字符
	 */
	public static synchronized String format(double number, String ex) {
		return getFormat(ex).format(number);
	}

	/**
	 * 格式 @param number 数字
	 * 
	 * @param ex
	 *            格式表达 
	 * @return 数字字符
	 */
	public static synchronized String format(long number, String ex) {
		return getFormat(ex).format(number);
	}

	/**
	 * 解析
	 * 
	 * @param source
	 *            数字字符 
	 * @param ex 格式表达 
	 * @return 数字
	 * @throws ParseException
	 */
	public static synchronized Number parse(String source, String ex) {
		NumberFormat nf= getFormat(ex);
		Number n;
		try {
			n = nf.parse(source);
			return n;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * 返回保留小数点后几位的float数
	 * 
	 * @param f 
	 * @param n 保留小数位数
	 * @return 返回结果四舍五入
	 */
	public static synchronized float parseFloat(float f, int n) {
		BigDecimal b = new BigDecimal(f);
		float f1 = b.setScale(n, BigDecimal.ROUND_HALF_UP).floatValue();
		return f1;
	}
	
	/**
	 * 把价格格式化为小数点后两位
	 * @param f 价格数字
	 * @return 格式化后的字符串
	 */
	public static synchronized String formatEterm(float f){
		return format(f, "0.00");
	}
	
	/**
	 * 票价四舍五入到10位
	 * @param f 原票价
	 * @return 四舍五入后的票价
	 */
	public static synchronized float tktPrice(float f){
		long L=Math.round(f);
		long L2=L/10*10;
		long yu=L%10;
		if(yu>=5){
			L2=L2+10;
		}
		return Float.parseFloat(L2+".00");
	}
	
	/**
	 * 费率四舍五入到个位
	 * @param f
	 * @return
	 */
	public static synchronized float tktFee(float f){
		long L=Math.round(f);
		return Float.parseFloat(L+".00");
	}

	public static void main(String[] args) {
		System.out.println(tktPrice(5070.0f/2));
	}

}
