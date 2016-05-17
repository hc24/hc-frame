package com.hc24.frame.spring.web.controller.config;

import java.beans.PropertyEditorSupport;
import org.springframework.util.StringUtils;

/**
 * float类型的属性转化器
 * @author hc24
 *
 */
public class FloatConvertEditor extends PropertyEditorSupport {	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (!StringUtils.hasText(text)) {
			setValue(-1f);
		} else{
			setValue(Float.parseFloat(text));
		}
	}
}
