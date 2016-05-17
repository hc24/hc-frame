package com.hc24.frame.util;

import java.math.BigDecimal;
/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 * 确的浮点数运算，包括加减乘除和四舍五入
 */
public class ArithUtil{
    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;
    //这个类不能实例化
    private ArithUtil(){
    }
 
    /**
     * 提供精确的加法运算�?
     * @param v1 被加�?
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    /**
     * 提供精确的减法运算�?
     * @param v1 被减�?
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    } 
    /**
     * 提供精确的乘法运算�?
     * @param v1 被乘�?
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
 
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以�?0位，以后的数字四舍五入�?
     * @param v1 被除�?
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1,double v2){
        return div(v1,v2,DEF_DIV_SCALE);
    }
 
    /**
     * 提供（相对）精确的除法运算�?当发生除不尽的情况时，由scale参数�?
     * 定精度，以后的数字只入不舍�?
     * @param v1 被除�?
     * @param v2 除数
     * @param scale 表示表示�?��精确到小数点以后几位�?
     * @return 两个参数的商
     */
    public static double div(double v1,double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_CEILING).doubleValue();
    }
 
    /**
     * 提供精确的小数位四舍五入处理�?
     * @param v �?��四舍五入的数�?
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double roundHalfUp(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 提供精确的小数位只入不舍处理�?
     * @param v �?��只入不舍的数�?
     * @param scale 小数点后保留几位
     * @return 只入不舍后的结果
     */
    public static double roundCeiling(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_CEILING).doubleValue();
    }
    
    public static void main(String[] args) {
		System.out.println(ArithUtil.mul(3.33, 6.278));
		System.out.println(ArithUtil.roundHalfUp(ArithUtil.mul(3.33, 6.278),0));
	}
}
 
 
