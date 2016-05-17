package com.hc24.frame.spring.web.controller.config;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.hc24.frame.spring.dao.bean.Bean;

public class BeanMethodArgumentResolver implements
		HandlerMethodArgumentResolver {

	
	public Object resolveArgument(MethodParameter mp,
			ModelAndViewContainer mvc, NativeWebRequest nwr,
			WebDataBinderFactory wdbf) throws Exception {
		 //HttpServletRequest request = nwr.getNativeRequest(HttpServletRequest.class);
		Bean bean=new Bean(); 
		Iterator<String> it = nwr.getParameterNames();
		while(it.hasNext()){
			String key = it.next();
			String val=nwr.getParameter(key);
			//非空的值才加入到
			if(!StringUtils.isEmpty(val)){
				bean.set(key,val);
			}else{
				bean.set(key, null);
			}
			
				
		}
		return bean;
	}

	
	public boolean supportsParameter(MethodParameter mp) {
		
		return Bean.class.isAssignableFrom(mp.getParameterType());
	}
	
	public static void main(String[] args) {
		System.out.println(Map.class.isAssignableFrom(Bean.class));
	}

}
