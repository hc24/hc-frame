package com.hc24.frame.util;


/**
 * 身份证处理工具
 * @author hc24
 *
 */
public class IDCodeUtil{
  
  /**
   * 根据身份证号获取类型，ADT成人，CHD儿童，INF婴儿
   * @param id
   * @return
   */
  public static String getType(String id){
    return getTypeByBirthday(getYmd(id));
  }
  
  /**
   * 根据出生日期获取类型，ADT成人，CHD儿童，INF婴儿
   * @param id
   * @return
   */
  public static String getTypeByBirthday(String birthday){
    String type = "ADT";
    try{
      String ymd = birthday;
      int year = DateUtil.year(DateUtil.ymd2Date(ymd));
      int yearNow = DateUtil.year(DateUtil.now());
      int age = yearNow-year;
      if(age>=2&&age<12){
        type = "CHD";
      }else if(age<2){
        type = "INF";
      }
    }catch(Exception e){}
    return type;
  }
  
  /**
   * 根据身份证号获取生日
   * @param id
   * @return
   */
  public static String getYmd(String id){
    if(id.length()==18){
      String ymd = id.substring(6,14);
      String y = ymd.substring(0,4);
      String m = ymd.substring(4,6);
      String d = ymd.substring(6,8);
      return y+"-"+m+"-"+d;
    }else if(id.length()==15){
      String ymd = id.substring(6,12);
      String y = ymd.substring(0,2);
      String m = ymd.substring(2,4);
      String d = ymd.substring(4,6);
      return "19"+y+"-"+m+"-"+d;
    }
    return "";
  }

  /**
   * 根据身份证号获取性别，0未知，1男，2女
   * @param id
   * @return
   */
  public static int getGender(String id){
    String gd = "";
    if(id.length()==18){
      gd = id.substring(16,17);
    }else if(id.length()==15){
      gd = id.substring(14,15);
    }
    if(gd.length()<1){
      return 0;
    }
    return Integer.parseInt(gd)%2==1?1:2;
  }
}