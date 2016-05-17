package com.hc24.frame.spring.web.controller.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hc24.frame.spring.dao.bean.DataGridModel;
import com.hc24.frame.util.ObjectUtil;



/**
 * 非ajax分页的父类
 * @author hc24
 *
 */
public class PageController {
	
	public void page(HttpServletRequest request,DataGridModel dgm){
		Map<String,Object> mm = new HashMap<String,Object>();
		Map<String, String[]> m = request.getParameterMap();
		Iterator<String> it = m.keySet().iterator();
		while(it.hasNext()){
			String key = it.next()+"";
			mm.put(key, request.getParameter(key));
		}
		mm.remove("page");
		mm.remove("rows");
		try {
			request.setAttribute("requestParams",ObjectUtil.encode(mm));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
