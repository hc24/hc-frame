package com.hc24.frame.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具
 * 
 * @author hc24
 * 
 */
public class StringUtil {
	
	private StringUtil(){}
	
	/**
	 * 将指定字符串进行转码
	 * 
	 * @param s 要转码的字符
	 * @param ocharSet 原编
	 * @param ncharSet 新编
	 * @return 转码后的字符,转码失败返回null
	 */
	public synchronized static String format(String s,String ocharSet,String ncharSet){
		try {
			return s==null?s:new String(s.getBytes(ocharSet),ncharSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将ISO-8859-1编码的字符串转换成UTF-8编码，用于中文转
	 * 适用于在tomcat中使用get方式提交会产生乱
	 * @param s 要转码的字符
	 * @return 转码后的中文字符,转码失败返回null
	 */
	public static String format(String s){
		return format(s,"ISO-8859-1","UTF-8");
	}

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param s 原文
	 * @return 密文,大写形式
	 * @throws Exception
	 */
	public static String md5(String s){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		md.update(s.getBytes());
		byte[] b = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			hex = (hex.length()==1?"0":"")+hex;
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
     * 对ajax提交来的数据进行转码
     * @param s 原字符串
     * @return 转码后的字符,转码失败返回null
     */
    public static String ajax(String s){
        try {
			return null==s?null:java.net.URLDecoder.decode(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * 将浮点数字格式化为字符串
     * @param f
     * @param pattern
     * @return
     */
    public static String formatNumber(double f, String pattern) {  
        java.text.DecimalFormat num = new DecimalFormat(pattern);  
        return num.format(f);  
    }
    
    /**
     * 根据提供的正则表达式返回匹配的第一个字符串
     * @param str 查找的字符串
     * @param regex 正则表达式
     * @return 没有则返回null
     */
    public static String pattern(String str,String regex){
    	Pattern p=Pattern.compile(regex);
    	Matcher m=p.matcher(str);
    	if(m.find()){
    		return m.group();
    	}
    	return null;
    }
    
    /**
     * 验证数字是否为数字（整数，负数，浮点数）
     * @param str 要验证的字符串
     * @return
     */
    public static boolean isNumeric(String str){
    	 Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+"); 
    	   Matcher isNum = pattern.matcher(str);
    	   if( !isNum.matches() ){
    	       return false; 
    	   } 
    	   return true; 
    }
    
    /**
     * 检测字符串是否为空
     * @param str
     * @return 为null或“”返回true
     */
    public static boolean isEmpty(String str){
    	return str==null||"".equals(str.trim());
    }
    
    
    
    public static void main(String[] args) {
		System.out.println(md5("12345a"));
		System.out.println(isNumeric("5.0"));
	}
	
}