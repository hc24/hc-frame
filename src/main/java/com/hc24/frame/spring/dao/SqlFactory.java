package com.hc24.frame.spring.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hc24.frame.spring.exception.DaoException;

/**
 * Sql工厂,根据不同数据库类型返回对应的sql
 * 
 * @author hc24
 * 
 */
public class SqlFactory {

	private static final Log logger = LogFactory.getLog(SqlFactory.class);

	/** 数据库类型 */
	public static DB db;

	/** 数据库枚举 */
	enum DB {
		MYSQL, ORACLE
	};

	/**
	 * 获得分页的sql
	 * 
	 * @return
	 */
	public static String getPageSql() {
		initDB();
		switch (db) {
		case MYSQL:
			return "%s limit ?,?";
		case ORACLE:
			return "select * from (select rownum n, t.* from (%s) t where rownum<?) where n>=?";
		default:
			throw new DaoException(db + "类型不存在");
		}
	}

	/**
	 * 获得meta数据的sql
	 */
	public static String getMetaSql(String tableName) {
		initDB();
		switch (db) {
		case MYSQL:
			return "show columns from "+tableName;
		case ORACLE:
			return "select t.column_name Field from user_tab_columns t where table_name='"+tableName+"'";
		default:
			throw new DaoException(db + "类型不存在");
		}
	}

	private static void initDB() {
		if (db == null) {
			try {
				logger.debug("检测是否有mysql数据库驱动程序");
				Class.forName("com.mysql.jdbc.Driver");
				db = DB.MYSQL;
			} catch (ClassNotFoundException e) {
				logger.warn("mysql驱动程序不存在");
			}
			try {
				logger.debug("检测是否有oracle数据库驱动程序");
				Class.forName("oracle.jdbc.driver.OracleDriver");
				db = DB.ORACLE;
			} catch (ClassNotFoundException e) {
				logger.warn("oracle驱动程序不存在");
			}
		}

		if (db == null)
			throw new DaoException("请增加数据库驱动程序jar");
	}

}
