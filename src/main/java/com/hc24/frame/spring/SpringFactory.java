package com.hc24.frame.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringFactory implements ApplicationContextAware {
	private static ApplicationContext context;// 声明一个静态变量保存

	@Override
	public void setApplicationContext(ApplicationContext contex) throws BeansException {
		synchronized (contex) {
			context = contex;
		}
		
	}

	public static ApplicationContext getContext() {
		if(context==null)
			context=new ClassPathXmlApplicationContext("applicationContext_mongo.xml");
		return context;
	}
	
	public static Object getBean(String id){
		return getContext().getBean(id);
	}
	
}
