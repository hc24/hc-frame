package com.hc24.frame.util.db;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.ResultSet;

import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * 数据库连接工具
 * 
 * 
 * 
 * @author hc24
 * 
 * 
 */

public class ConnUtil {
	private static final Log logger=LogFactory.getLog(ConnUtil.class);

	/** 数据库连接URL */
	//private static String DB_URL="jdbc:oracle:thin:@10.6.183.225:1523:tvb2c";
	
	private static String DB_URL="jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.6.183.225)(PORT=1523)))(CONNECT_DATA=(SERVICE_NAME=tvb2c)))";
	/** 数据库连接驱动 */
	private static String DB_DRIVER="oracle.jdbc.driver.OracleDriver";
	/** 数据库用户名 */
	private static String DB_USERNAME="pypeng";
	/** 数据库密码 */
	private static String DB_PASSWORD="swky5050123";

	private static ConnUtil connUtil = null;

	/**
	 * 
	 * 构造函数，必须是private
	 */

	private ConnUtil() {
		//读取数据库配置
//		Properties p=new Properties();
//		try {
//			p.load(Eterm.class.getResourceAsStream("/jdbc.properties"));
//			DB_URL=p.getProperty("jdbc.driverUrl");
//			DB_DRIVER=p.getProperty("jdbc.driver");
//			DB_USERNAME=p.getProperty("jdbc.user");
//			DB_PASSWORD=p.getProperty("jdbc.password");
//		}catch(Exception e){
//			logger.error("读取数据配置出错"+e.toString());
//		}
	}

	/**
	 * 
	 * 连接数据库
	 * 
	 * 
	 * 
	 * @return 数据库连接对象
	 * 
	 * @throws Exception
	 */

	public Connection getCon() throws Exception {

		Connection con = null;

		try {

			Class.forName(DB_DRIVER);

			con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			
			System.out.println("====数据库连接成功====");

		} catch (Exception e) {

			e.printStackTrace();

			throw new Exception("获取数据库连接失败：" + e.getMessage());

		}

		return con;

	}

	/**
	 * 
	 * 关闭数据库连接
	 * 
	 * 
	 * 
	 * @param rs
	 * 
	 * @param stmt
	 * 
	 * @param con
	 * 
	 * @throws Exception
	 */

	public void closeCon(ResultSet rs, Statement stmt, Connection con)

	throws RuntimeException {

		try {

			if (rs != null) {

				rs.close();

			}

			if (stmt != null) {

				stmt.close();

			}

			if (con != null) {

				con.close();

			}

		} catch (Exception e) {

			e.printStackTrace();

			throw new RuntimeException("数据库连接关闭失败：" + e.getMessage());

		}

	}

	/**
	 * 
	 * 获得数据库连接工具类
	 * 
	 * 
	 * 
	 * @return
	 */

	public static ConnUtil newInstance() {

		if (connUtil == null) {

			connUtil = new ConnUtil();

		}

		return connUtil;

	}
	
	public static void main(String[] args) {
		try {
			ConnUtil.newInstance().getCon();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
