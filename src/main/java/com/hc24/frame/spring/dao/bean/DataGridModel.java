package com.hc24.frame.spring.dao.bean;

/**
 * 封装了EasyUI的DataGrid对象的分页信息和排序信息
 * 
 * @author hc24
 * 
 */
public class DataGridModel implements java.io.Serializable {

	private static final long serialVersionUID = 7232798260610351343L;
	/** 当前页,名字必须为page */
	private int page=1;
	/** 每页大小,名字必须为rows,EasyUI中返回数据也叫rows(Page对象中定义)，真奇怪，为什么搞成一样的，别扭~~ */
	private int rows=10;
	/** 排序字段,暂不考虑排序功能 */
	private String sort;
	/** 排序规则 */
	private String order="desc";

	/** 得到当前页 */
	public int getPage() {
		return page;
	}
	
	/** 设置当前页 */
	public void setPage(int page) {
		this.page = page;
	}

	/** 得到每页大小 */
	public int getRows() {
		return rows;
	}
	
	/** 设置每页大小 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	/** 得到排序字段 */
	public String getSort() {
		return sort;
	}

	/** 设置排序字段 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/** 得到排序规则 */
	public String getOrder() {
		return order;
	}

	/** 设置排序规则 */
	public void setOrder(String order) {
		this.order = order;
	}

}
