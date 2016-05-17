package com.hc24.frame.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link BeanInfo}池，避免重复创建，提高效率
 * @author hc24
 *
 */
public class BeanInfoPool {

	/** BeanInfo缓存 */
	private static Map<Class<?>, BeanInfo> pool = new HashMap<Class<?>, BeanInfo>();

	/**
	 * 缓存以来提高效率
	 * 
	 * @param klass
	 * @return
	 */
	public static BeanInfo getBeanInfo(Class<?> clazz) {
		BeanInfo info = pool.get(clazz);
		if (null == info) {
			try {
				info = Introspector.getBeanInfo(clazz, Object.class);
			} catch (IntrospectionException e) {
				throw new RuntimeException(e);
			}
			pool.put(clazz, info);
		}
		return info;
	}
	
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz,String propertyName){
		BeanInfo beanInfo=getBeanInfo(clazz);
		for(PropertyDescriptor pd:beanInfo.getPropertyDescriptors()){
			if(pd.getName().equals(propertyName)){
				return pd;
			}
		}
		throw new RuntimeException(clazz.getName()+"没有找到"+propertyName+"属性");
	}
}
