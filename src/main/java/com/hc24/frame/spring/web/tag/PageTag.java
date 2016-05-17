package com.hc24.frame.spring.web.tag;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.hc24.frame.spring.dao.bean.Page;
import com.hc24.frame.util.ObjectUtil;

/**
 * 自定义分页标签
 * @author hc24
 * @version 2011-12-29
 */
public class PageTag extends TagSupport {
	
	private static final long serialVersionUID = 1L;
	private static final Logger logger=Logger.getLogger(PageTag.class);
	/** Page对象默认KEY的名称 */
	private static final String PAGE_KEY_NAME="page";
	/** 查询参数默认的KEY的名称 */
	private static final String REQUEST_PARAMS="requestParams";
	/** 默认分页大小 */
	private static final int DEFAULT_PAGE_SIZE=10;
	
	/** 每页的大小,可以为空 */
	private int pageSize=DEFAULT_PAGE_SIZE;
	/** 查询的url,可以为空 */
	private String url;
	
	/**
	 * 输出分页
	 */
	@Override
	public int doStartTag() throws JspException {
		try {

			HttpServletRequest request= (HttpServletRequest) pageContext.getRequest();
			Page<?> page=(Page<?>) request.getAttribute(PAGE_KEY_NAME);
			if(page==null)
				return SKIP_BODY;
//			pageSize=page.getTotalCount()%page.getLastPageNo()==0?page.getTotalCount()/page.getLastPageNo()
//					:page.getTotalCount()/page.getLastPageNo()+1;
			if(url==null||"".equals(url)){
				url=request.getRequestURI();
			}
//			String queryStr=request.getQueryString();
//			if(queryStr!=null)
//				url=url+"?"+queryStr;
			
			/* 构建form */
			StringBuilder formBuilder=new StringBuilder();
			formBuilder.append("<form id='tvPagerForm' method='post' action='").append(url).append("' style='display:none;'>");
			String requestParams=(String) request.getAttribute(REQUEST_PARAMS);
			@SuppressWarnings("unchecked")
			Map<String, Object> mm = (Map<String, Object>) ObjectUtil.decode(requestParams);		
			Iterator<String> it=mm.keySet().iterator();
			while(it.hasNext()){
				String attrKey=it.next();
				//Object attrValue=request.getAttribute(attrKey);
				Object attrValue=mm.get(attrKey);
				formBuilder.append("<input type='hidden' name='").append(attrKey).append("' value='").append(attrValue).append("'/>");
								
			}
			formBuilder.append("<input type='hidden' name='rows' value='").append(pageSize).append("' id='tvPageSize'/>");
			formBuilder.append("<input type='hidden' name='page' value='' id='tvCurPageNo'/>");
			formBuilder.append("</form>");
			
			/* 构建Javascript */
			StringBuilder jsBuilder=new StringBuilder("<script type='text/javascript'>function tvGoPage(curPageNo){");
			jsBuilder.append("var curPageNoInput=document.getElementById('tvCurPageNo');");
			jsBuilder.append("var lastPageNo=").append(page.getLastPageNo()).append(";");
			jsBuilder.append("if(curPageNo<1){curPageNo=1;}else if(curPageNo>lastPageNo){curPageNo=lastPageNo;}");
			jsBuilder.append("curPageNoInput.value=curPageNo;document.getElementById('tvPagerForm').submit();}</script>");
			
			/* 构建分页信息 */
			String pageTextFormat="<div style='font-size:12px;'><span style='color:#999;margin-right:10px;'>每页%s条</span><span style='color:#999;margin-right:10px;'>共%s条</span><span  style='color:#999;margin-right:10px;'>%s/%s</span>" +
			"<a style='color:red;margin-right:3px;text-decoration: none;' href='javascript:void(0);' onclick='tvGoPage(%s)' >首页</a><a style='color:red;margin-right:3px;text-decoration: none;' href='javascript:void(0);' onclick='tvGoPage(%s)'>上一页</a>" +
			"<a style='color:red;margin-right:3px;text-decoration: none;' href='javascript:void(0);' onclick='tvGoPage(%s)'>下一页</a><a style='color:red;margin-right:3px;text-decoration: none;' href='javascript:void(0);' onclick='tvGoPage(%s)'>尾页</a></div>";
			int curPageNo=page.getCurrentPageNo();
			String pageText= String.format(pageTextFormat, pageSize,page.getTotal(),page.getCurrentPageNo(),
					page.getLastPageNo(),1,(curPageNo-1),(curPageNo+1),page.getLastPageNo());
		
			/* 输出 */
			pageContext.getOut().write(pageText+formBuilder.toString()+jsBuilder.toString());
		} catch (Exception ee) {
			logger.error("输出分页出错", ee);			
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;//正常执行接下来的页面
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	

}
