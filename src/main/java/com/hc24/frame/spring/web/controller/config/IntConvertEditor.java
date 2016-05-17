package com.hc24.frame.spring.web.controller.config;

import java.beans.PropertyEditorSupport;
import org.springframework.util.StringUtils;

/**
 * int类型的属性转化器，为了避免和java成员变量默认为0的”误会“
 * @author hc24
 *
 */
public class IntConvertEditor extends PropertyEditorSupport {	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (!StringUtils.hasText(text)) {
			setValue(-1);
		} else{
			setValue(Integer.parseInt(text));
		}
	}
}
