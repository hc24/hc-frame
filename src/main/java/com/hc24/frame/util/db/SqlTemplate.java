package com.hc24.frame.util.db;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



/**
 * sql执行模版类，通过出入sql执行execute方法得到需要的数据
 * @author hc24
 *
 */
public class SqlTemplate {

	private String sql;

	public SqlTemplate(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}



	public Object execute(){
		return execute(null,null);
	}
	
	public Object execute(ResultCallBack resultCallBack){
		return execute(resultCallBack,null);
	}

	public Object execute(StmtCallBack stmtCallBack){
		return execute(null,stmtCallBack);
	}
	
	/**
	 * 执行sql的核心方法
	 * @param resultCallBack ResultSet个性化处理接口
	 * @param stmtCallBack PrepareStatement个性化赋值参数接口
	 * @return 返回需要的数据
	 */
	public Object execute(ResultCallBack resultCallBack,StmtCallBack stmtCallBack) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ConnUtil connUtil = null;
		
		try {
			//①获得连接
			connUtil = ConnUtil.newInstance();// 获得连接工具
			con = connUtil.getCon();
			System.err.println("连接数据库成功...");
				   
			//②得到和设置Statement
			stmt = con.prepareStatement(sql);	
			if(stmtCallBack!=null){
				stmtCallBack.setValue(stmt);//PrepareStatement的个性化赋值处理留给回调函数
			}
			
			//③发送sql到数据库
			boolean r = stmt.execute();
			 
			//④对返回ResultSet进行处理返回需要的数据
			if(resultCallBack!=null){
				return resultCallBack.export(stmt.getResultSet());//ResultSet的个性化处理留给回调函数去处理
			}else{
				return r;//返回影响数据库的条数
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//⑤执行关闭连接操作
			connUtil.closeCon(rs, stmt, con);
		}
		return null;
	}
}

