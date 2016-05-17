package com.hc24.frame.util.db;



import java.sql.ResultSet;

import java.sql.SQLException;



/**

 * 回调借口

 * @author superchao

 *

 */

public interface ResultCallBack {

	/**

	 * 数据的实际操作

	 * 

	 * @param rs

	 * @throws SQLException 

	 */

	public Object export(ResultSet rs) throws SQLException;

}

