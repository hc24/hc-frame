package com.hc24.frame.spring.dao.bean;

/**
 * Bean转换规则接口
 *
 * @author 6tail
 *
 */
public interface IBeanRule{

  /**
   * 获取指定属性名对应的key
   *
   * @param property 属性名
   * @return key
   */
  String getKey(String property);
}