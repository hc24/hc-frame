package com.hc24.frame.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具
 * 
 * @author hc24
 */
public class DateUtil {
	
	/** 月份 */
	private static final Map<String, String> MONTH = new HashMap<String, String>();
	/** 星期 */
	private static final Map<String, String> WEEK = new HashMap<String, String>();
	/** 中文数字 */
	private static final Map<String, String> NUMBER = new HashMap<String, String>();
	
	static {
		MONTH.put("01", "JAN");
		MONTH.put("02", "FEB");
		MONTH.put("03", "MAR");
		MONTH.put("04", "APR");
		MONTH.put("05", "MAY");
		MONTH.put("06", "JUN");
		MONTH.put("07", "JUL");
		MONTH.put("08", "AUG");
		MONTH.put("09", "SEP");
		MONTH.put("10", "OCT");
		MONTH.put("11", "NOV");
		MONTH.put("12", "DEC");
		
		MONTH.put("JAN", "01");
		MONTH.put("FEB", "02");
		MONTH.put("MAR", "03");
		MONTH.put("APR", "04");
		MONTH.put("MAY", "05");
		MONTH.put("JUN", "06");
		MONTH.put("JUL", "07");
		MONTH.put("AUG", "08");
		MONTH.put("SEP", "09");
		MONTH.put("OCT", "10");
		MONTH.put("NOV", "11");
		MONTH.put("DEC", "12");
		
		WEEK.put(Calendar.SUNDAY+"", "日");
		WEEK.put(Calendar.MONDAY+"", "一");
		WEEK.put(Calendar.TUESDAY+"", "二");
		WEEK.put(Calendar.WEDNESDAY+"", "三");
		WEEK.put(Calendar.THURSDAY+"", "四");
		WEEK.put(Calendar.FRIDAY+"", "五");
		WEEK.put(Calendar.SATURDAY+"", "六");
		
		NUMBER.put("0", "零");
		NUMBER.put("1", "一");
		NUMBER.put("2", "二");
		NUMBER.put("3", "三");
		NUMBER.put("4", "四");
		NUMBER.put("5", "五");
		NUMBER.put("6", "六");
		NUMBER.put("7", "七");
		NUMBER.put("8", "八");
		NUMBER.put("9", "九");
	}

	private DateUtil(){}

	
	/**
	 * 获取当前时间
	 * 
	 * @return 当前时间
	 */
	public static Date now(){
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 将yyyy-MM-dd或yyyy/MM/dd格式的日期字符串转换为日期类型
	 * 
	 * @param ymd yyyy-MM-dd或yyyy/MM/dd格式的字符串
	 * @return 日期,转化失败返回null
	 */
	public static Date ymd2Date(String ymd) {
		String[] f = {"yyyy-MM-dd","yyyy/MM/dd"};
		for(String o : f){
			return parse(ymd,o);
		}
		return null;
	}
	
	/**
     * 将yyyy-MM-dd HH:mm:ss格式的日期字符串转换为日期类型
     * 
     * @param ymdhms yyyy-MM-dd HH:mm:ss格式的字符串
     * @return 日期  转换失败返回null
     */
    public static Date ymdhms2Date(String ymdhms)  {
        return parse(ymdhms,"yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 将指定格式字符串转换为日期
     * @param s 字符串
     * @param pattern 格式
     * @return 日期  转换失败返回null
     */
    public static Date parse(String s,String pattern) {
        try {
			return new SimpleDateFormat(pattern).parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	/**
	 * 将日期转换成yyyy-MM-dd格式的字符串
	 * 
	 * @param date 日期
	 * @return yyyy-MM-dd格式的字符串
	 */
	public static String ymd(Date date) {
		return format(date,"yyyy-MM-dd");
	}
	
	/**
	 * 将日期转换成HH:mm:ss格式的字符串
	 * 
	 * @param date 日期
	 * @return HH:mm:ss格式的字符串
	 */
	public static String hms(Date date) {
		return format(date,"HH:mm:ss");
	}
	
	/**
	 * 将日期转换成yyyy-MM-dd HH:mm:ss格式的字符串
	 * 
	 * @param date 日期
	 * @return yyyy-MM-dd HH:mm:ss格式的字符串
	 */
	public static String ymdhms(Date date) {
		return format(date,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 将日期转换成指定格式的字符串
	 * 
	 * @param date 日期
	 * @return 指定格式的字符串
	 */
	public static String format(Date date,String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * 指定类型往前推
	 * @param type 类型：年、月、日等
	 * @param date 参考日期
	 * @param length 长度
	 * @return 往前推的日期
	 */
	public static Date previous(int type,Date date,int length){
		return next(type,date,0-length);
	}
	
	/**
	 * 指定类型往后推
	 * @param type 类型：年、月、日等
	 * @param date 参考日期
	 * @param length 长度
	 * @return 往后推的日期
	 */
	public static Date next(int type,Date date,int length){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(type,length);
		return c.getTime();
	}
	
	/**
	 * 获得前几天的日期
	 * 
	 * @param date 参考日期
	 * @return 前几天的日期
	 */
	public static Date previous(Date date,int days){
		return previous(Calendar.DATE,date,days);
	}
	
	/**
	 * 获得后几天的日期
	 * 
	 * @param date 参考日期
	 * @return 后几天的日期
	 */
	public static Date next(Date date,int days){
		return next(Calendar.DATE,date,days);
	}
	
	/**
	 * 月份往后推的日期
	 * 
	 * @param date 参考日期
	 * @param months 推后几个月
	 * @return 月份往后推的日期
	 */
	public static Date nextMonth(Date date,int months){
		return next(Calendar.MONTH,date,months);
	}
	
	/**
	 * 月份往前推的日期
	 * 
	 * @param date 参考日期
	 * @param months 前推几个月
	 * @return 月份往前推的日期
	 */
	public static Date previousMonth(Date date,int months){
		return previous(Calendar.MONTH,date,months);
	}
	
	/**
	 * 年份往后推的日期
	 * 
	 * @param date 参考日期
	 * @param months 推后几个月
	 * @return 月份往后推的日期
	 */
	public static Date nextYear(Date date,int years){
		return next(Calendar.YEAR,date,years);
	}	
	
	/**
	 * 年份往前推的日期
	 * 
	 * @param date 参考日期
	 * @param months 前推几个月
	 * @return 月份往前推的日期
	 */
	public static Date previousYear(Date date,int years){
		return previous(Calendar.YEAR,date,years);
	}
	
	/**
	 * 某日期的某类型的数值 
	 * @param type 类型：年、月、日等
	 * @param date 日期
	 * @return 数值
	 */
	public static int get(int type,Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(type);
	}
	
	/**
	 * 获取年份
	 * @param date 日期
	 * @return 年份
	 */
	public static int year(Date date){
		return get(Calendar.YEAR,date);
	}
	
	/**
	 * 获取月份
	 * @param date 日期
	 * @return 月份，如01、10
	 */
	public static String month(Date date){
		int m = get(Calendar.MONTH,date)+1;
		return (m<10?"0":"")+m;
	}
	
	/**
	 * 获取天
	 * @param date 日期
	 * @return 天，如01、23
	 */
	public static String day(Date date){
		int d = get(Calendar.DATE,date);
		return (d<10?"0":"")+d;
	}
	
	
	/**
	 * 设置日期某类型的值
	 * @param date 日期
	 * @param type 类型，年/月/日/时/分/秒……
	 * @param value
	 * @return
	 */
	public static Date set(Date date,int type,int value){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.set(type, value);
		return c.getTime();
	}
	
	
	/**
	 * 将yyyy-MM-dd的格式字符串转化为Eterm的(22JUL,22JUL13)格式的日期字符串
	 * @param ymd yyyy-MM-dd格式的日期字符串
	 * @return Eterm日期格式
	 */
	public static String etermDate(String ymd){
		String y=ymd.substring(2,4);
		String m=MONTH.get(ymd.substring(5, 7));
		String d=ymd.substring(8);
		return d+m+y;
	}
	
	/**
	 * 日期字符串转换为YYYY-MM-DD格式
	 * @param dayMonthYear 支持的参数格式: 1MAY10 01MAY10 1MAY2010 01MAY2010，不区分大小写，以上转换后为2010-05-01
	 * @return YYYY-MM-DD格式的字符串
	 */
	public static String ymd(String dayMonthYear){
		return ymd(dayMonthYear,"20");
	}
	
	/**
	 * 日期字符串转换为YYYY-MM-DD格式的日期
	 * @param dayMonthYear 支持的参数格式: 1MAY10 01MAY10 1MAY2010 01MAY2010，不区分大小写，以上转换后为2010-05-01
	 * @return Date日期
	 */
	public static Date ymdDate(String dayMonthYear){
		return parse(ymd(dayMonthYear,"20"), "yyyy-MM-dd");
	}
	
	/**
	 * 日期字符串转换为YYYY-MM-DD格式
	 * @param dayMonthYear 支持的参数格式: 1MAY10 01MAY10 1MAY2010 01MAY2010  01MAY
	 * @param yearPrefix 年份前缀，如yearPrefix=19,1MAY09=1909-05-01
	 * @return YYYY-MM-DD格式的字符串
	 */
	public static String ymd(String dayMonthYear,String yearPrefix){
		String s = dayMonthYear.toUpperCase();
		String y="2013";
		String m="";
		String d="";
		//年日
		Pattern p = Pattern.compile("\\d+");
		Matcher matcher = p.matcher(s);
		if(matcher.find(0)){//日
			d=matcher.group(0);
			if(matcher.group(0).length()==1)
				d="0"+matcher.group(0);
			
		}
		if(matcher.find()){
			y=matcher.group();
			if(matcher.group().length()==2)
				y=yearPrefix+matcher.group();
		}
		//月
		p = Pattern.compile("[A-Z]{3}");
		matcher = p.matcher(s);
		if(matcher.find()){
			m=MONTH.get( matcher.group());
		}
		
//		switch(s.length()){
//			case 6:
//				s = "0"+s;
//				s = s.substring(0,5)+yearPrefix+s.substring(5);
//				break;
//			case 7:
//				s = s.substring(0,5)+yearPrefix+s.substring(5);
//				break;
//			case 8:
//				s = "0"+s;
//				break;
//		}
		s = y+"-"+m+"-"+d;
		return s;
	}
	
	/**
	 * 获取某日期的星期中文表示
	 * @param date 日期
	 * @return 日、一、二、三、四、五、六
	 */
	public static String weekCH(Date date){
		return WEEK.get(get(Calendar.DAY_OF_WEEK,date)+"");
	}
	
	/**
	 * 获取某日期的月份中文表示
	 * @param date 日期
	 * @return 日、一、二、三、四、五、六
	 */
	public static String monthCH(Date date){
		return new String[]{"一","二","三","四","五","六","七","八","九","十","十一","十二"}[get(Calendar.MONTH,date)];
	}
	
	/**
	 * 获取某日期的年份中文表示
	 * @param date 日期
	 * @return 年份，如二零一零
	 */
	public static String yearCH(Date date){
		String[] y = (year(date)+"").split("");
		StringBuffer s = new StringBuffer();
		for(String o:y){
			s.append(null==NUMBER.get(o)?"":NUMBER.get(o));
		}
		return s.toString();
	}
	
	/**
	 * 返回时刻
	 * @return
	 */
	public static String shike(){
		String str="";
		
		Date date=new Date();
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		int h= c.get(Calendar.HOUR_OF_DAY);
		if((18<h&&h<24)||(0<h&&h<6)){
			str="晚上";
		}else if(h>=6&&h<=8){
			str="早晨";
		}else if(h>8&&h<11){
			str="上午";
		}else if(h>=11&&h<13){
			str="中午";
		}else{
			str="下午";
		}
		return str;
	}
	
	/**
	 * 时间间隔天数
	 * @param startDate 起始日期
	 * @param endDate 结束日期
	 * @return 间隔天数
	 */
	public static int intervalDay(Date startDate,Date endDate){
		return (int) ((endDate.getTime()-startDate.getTime())/(1000*60*60*24));
	}
	
	/**
	 * 时间间隔小时数
	 * @param startDate 起始日期
	 * @param endDate 结束日期
	 * @return 间隔天数
	 */
	public static int intervalHours(Date startDate,Date endDate){
		return (int) ((endDate.getTime()-startDate.getTime())/(1000*60*60));
	}
	
	/**
	 * 时间间隔分钟数
	 * @param startDate 起始日期
	 * @param endDate 结束日期
	 * @return 间隔天数
	 */
	public static int intervalMinutes(Date startDate,Date endDate){
		return (int) ((endDate.getTime()-startDate.getTime())/(1000*60));
	}
	
	/**
	 * 时间间隔秒数
	 * @param startDate 起始日期
	 * @param endDate 结束日期
	 * @return 间隔天数
	 */
	public static int intervalSeconds(Date startDate,Date endDate){
		return (int) ((endDate.getTime()-startDate.getTime())/(1000));
	}
	
	/**
	 * 判断航班日期是否是婴儿[2,12)
	 * @param flight 航班日期  yyyy-MM-dd
	 * @param birth 儿童生日 yyyy-MM-dd
	 * @return
	 */
	public static boolean isChild(String flight,String birth){
		Date flightDate= parse(flight, "yyyy-MM-dd");
		Date birthDate= parse(birth, "yyyy-MM-dd");
		Calendar flightCal= Calendar.getInstance();
		flightCal.setTime(flightDate);
		Calendar birthCal= Calendar.getInstance();
		birthCal.setTime(birthDate);
		
		int y= flightCal.get(Calendar.YEAR)-birthCal.get(Calendar.YEAR);
		int m= flightCal.get(Calendar.MONTH)-birthCal.get(Calendar.MONTH);
		int d= flightCal.get(Calendar.DATE)-birthCal.get(Calendar.DATE);
		if(m<0){
			y--;
		}
		if(m>=0&&d<0){
			y--;
		}
		if(y>=2&&y<12){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 计算年龄
	 * @param birth yyyy-MM-dd
	 * @return
	 */
	public static int calcAge(String birth){
		Date nowDate= new Date();
		Date birthDate= parse(birth, "yyyy-MM-dd");
		Calendar flightCal= Calendar.getInstance();
		flightCal.setTime(nowDate);
		Calendar birthCal= Calendar.getInstance();
		birthCal.setTime(birthDate);
		
		int y= flightCal.get(Calendar.YEAR)-birthCal.get(Calendar.YEAR);
		int m= flightCal.get(Calendar.MONTH)-birthCal.get(Calendar.MONTH);
		int d= flightCal.get(Calendar.DATE)-birthCal.get(Calendar.DATE);
		if(y<0){
			throw new RuntimeException("您老还没出生");
		}
		
		if(m<0){
			y--;
		}
		if(m>=0&&d<0){
			y--;
		}

		return y;
	}
	
	/**
	 * 计算年龄
	 * @param birthDate
	 * @return
	 */
	public static int calcAge(Date birthDate){
		Date nowDate= new Date();
		Calendar flightCal= Calendar.getInstance();
		flightCal.setTime(nowDate);
		Calendar birthCal= Calendar.getInstance();
		birthCal.setTime(birthDate);
		
		int y= flightCal.get(Calendar.YEAR)-birthCal.get(Calendar.YEAR);
		int m= flightCal.get(Calendar.MONTH)-birthCal.get(Calendar.MONTH);
		int d= flightCal.get(Calendar.DATE)-birthCal.get(Calendar.DATE);
		if(y<0){
			throw new RuntimeException("您老还没出生");
		}
		
		if(m<0){
			y--;
		}
		if(m>=0&&d<0){
			y--;
		}

		return y;
	}
	
	/**
	 * 身份证号
	 * @param idCard 身份证号
	 * @return
	 */
	public static String getBirthByIdCard(String idCard) { 
        String idCardNumber = idCard.trim(); 
        int idCardLength = idCardNumber.length(); 
        String birthday = null; 
        if (idCardNumber == null || "".equals(idCardNumber)) { 
            return null; 
        } 
        if (idCardLength == 18) { 
            birthday = idCardNumber.substring(6, 10) + "-" 
                    + idCardNumber.substring(10, 12) + "-" 
                    + idCardNumber.substring(12, 14); 
        } 
        if (idCardLength == 15) { 
            birthday = "19" + idCardNumber.substring(6,8) + "-" 
                    + idCardNumber.substring(8, 10) + "-" 
                    + idCardNumber.substring(10, 12); 
        } 
        return birthday; 
    } 
	
	
	public static void main(String[] args) {
		System.out.println(yearCH(now())+"年"+monthCH(now())+"月");
		System.out.println(ymd("23Jul"));
		System.out.println(calcAge("2012-01-04"));
		System.out.println(getBirthByIdCard("371481198703174818"));
	}
}