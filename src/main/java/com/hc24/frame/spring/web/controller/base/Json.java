package com.hc24.frame.spring.web.controller.base;

/**
 * ajax操作结果返回封装
 * 
 * @author hc24
 * 
 */
public class Json {
	/** 操作是否成功,默认是true */
	private boolean success=true;
	/** 操作失败返回的提示信息 */
	private String msg="操作成功";
	
	private Object data;

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
