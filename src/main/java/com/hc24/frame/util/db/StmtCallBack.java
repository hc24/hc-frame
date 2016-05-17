package com.hc24.frame.util.db;

import java.sql.PreparedStatement;

/**
 * 为PrepareStatement做个性化赋值处理
 * @author hc24
 *
 */
public interface StmtCallBack {
	/**
	 * 为stmt设置sql的参数
	 * @param stmt
	 */
	public void setValue(PreparedStatement stmt);
}
