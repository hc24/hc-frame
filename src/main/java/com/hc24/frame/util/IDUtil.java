package com.hc24.frame.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义ID生成器，推荐使用{@link UUID}
 * @author hc24
 *
 */
public class IDUtil{

	/** 每秒自增序列 */
    private static int serial = 0;

    /** 当前时间字符�?*/
    private static String time = "";

    /** 自增序列位数 */
    private static final int DIGIT = 3;
    
    private IDUtil(){}

    /**
     * 获取新的不重复的ID
     * 
     * @return 15位长整型数字
     */
    public synchronized static long next(){
        String s = "";
        String t = new SimpleDateFormat("yyMMddHHmmss").format(new Date(System.currentTimeMillis()));
        if(!t.equals(time)){
            time = t;
            serial = 0;
        }
        int max = (int) Math.pow(10, DIGIT);
        if(++serial >= max){
        	try{
        		Thread.sleep(1000L);
        	}catch(InterruptedException e){}
        	return next();
        }
        s += serial;
        while(s.length() < DIGIT){
            s = "0" + s;
        }
        return Long.parseLong(time + s);
    }
    
    private static int sequence=100;
    
    /**
     * 根据当前时间产生订单ID,同一秒钟可以产生900个不同的ID
     * @return  yyyyMMddHHmmss+xxxx
     */
    public synchronized static String orderID(String prefix){
    	Date date=new Date();
    	String idStr = DateUtil.format(date, "yyyyMMddHHmmss")+sequence;
    	sequence++;
    	if(sequence>999)
    		sequence=100;
    	return prefix+idStr;
    }
    
    
    public synchronized static String orderSmsID(){
    	Date date=new Date();
    	String idStr = DateUtil.format(date, "yyyyMMddHHmmss")+sequence;
    	sequence++;
    	if(sequence>999)
    		sequence=100;
    	return idStr+"_EXSMS";
    }
    
    public static void main(String[] args) {
		System.out.println(IDUtil.next());
		for(int i=0;i<100;i++){
			new Thread(new Runnable() {
				
				public void run() {
					System.out.println(orderID("OR"));
					
				}
			}).start();
			
		}
//		System.out.println(orderID("SMS"));
//		System.out.println(orderID("SMS"));
//		System.out.println(orderID("SMS"));
//		System.out.println(orderID("SMS"));
//		System.out.println(orderID("SMS"));
//		System.out.println(orderID("SMS"));
//		System.out.println(orderID("SMS"));
//		System.out.println(orderID("SMS"));
//		System.out.println(orderID("SMS"));
		
		
	}
}