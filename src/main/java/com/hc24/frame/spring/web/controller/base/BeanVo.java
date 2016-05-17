package com.hc24.frame.spring.web.controller.base;

import com.hc24.frame.spring.dao.bean.Bean;

/**
 * {@link Bean}的包装类
 * @author hc24
 *
 */
public class BeanVo {
	private Bean bean=new Bean();

	public Bean getBean() {
		return bean;
	}

	public void setBean(Bean bean) {
		this.bean = bean;
	}

}
