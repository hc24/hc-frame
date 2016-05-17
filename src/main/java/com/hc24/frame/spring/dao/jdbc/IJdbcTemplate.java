package com.hc24.frame.spring.dao.jdbc;

import java.util.List;
import java.util.Map;

import com.hc24.frame.spring.dao.IBaseTemplate;
import com.hc24.frame.spring.dao.bean.Bean;
import com.hc24.frame.spring.dao.bean.Page;
import com.hc24.frame.spring.exception.DaoException;

/**
 * Sql数据库查询&分页接口
 * 
 * @author hc24
 * 
 */
public interface IJdbcTemplate extends IBaseTemplate {

	/**
	 * 执行sql（such as an insert, update or delete statement）
	 * @param sql sql
	 * @param args 参数
	 * @return
	 */
	public int update(String sql,Object... args);
	
	public <T> T queryForObject(String sql, Map<String, ?> paramMap,
			Class<T> requiredType) throws DaoException;

	public <T> List<T> queryObjectList(String sql, Object[] params,
			Class<T> requiredType);

	public Bean queryForBean(String sql, Object[] params) throws DaoException;

	public List<Bean> queryBeanList(String sql, Object[] params);

	/**
	 * queryMapByPage的重载方法,sql不包含查询参数
	 * 
	 * @param sql
	 * @param pageSize
	 * @param curPageNo
	 * @return
	 */
	public Page<Map<String, Object>> queryMapByPage(String sql, int pageSize,
			int curPageNo);

	/**
	 * 根据查询sql（sql如果包括查询参数采用?号占位符的形式）和分页参数以及查询sql的查询参数得到Page对象
	 * Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param sql
	 *            查询sql
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @param args
	 *            包含sql中?号占位符的参数数组,如果没有参数则传递null
	 * @return Page对象里面的result对象是List<Map<String,Object>>的形式
	 */
	public Page<Map<String, Object>> queryMapByPage(String sql, int pageSize,
			int curPageNo, List<Object> params);

	/**
	 * 根据查询sql（sql如果包括查询参数采用?号占位符的形式）和分页参数以及查询sql的查询参数得到Page对象
	 * Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param sql
	 *            查询sql
	 * @param countSql
	 *            查询总数的sql
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @param args
	 *            包含sql中?号占位符的参数数组,如果没有参数则传递null
	 * @return Page对象里面的result对象是List<Map<String,Object>>的形式
	 */
	public Page<Map<String, Object>> queryMapByPage(String sql,
			String countSql, int pageSize, int curPageNo, List<Object> params);

	/**
	 * 根据查询sql（sql如果包括查询参数采用?号占位符的形式）和分页参数以及查询sql的查询参数得到Page对象
	 * Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param sql
	 *            查询sql
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @return Page对象里面的result对象是List<Bean>的形式
	 */
	public Page<Bean> queryBeanByPage(String sql, int pageSize, int curPageNo);

	/**
	 * 根据查询sql（sql如果包括查询参数采用?号占位符的形式）和分页参数以及查询sql的查询参数得到Page对象
	 * Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param sql
	 *            查询sql
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @param args
	 *            包含sql中?号占位符的参数数组,如果没有参数则传递null
	 * @return Page对象里面的result对象是List<Bean>的形式
	 */
	public Page<Bean> queryBeanByPage(String sql, int pageSize, int curPageNo,
			List<Object> params);

	/**
	 * 根据查询sql（sql如果包括查询参数采用?号占位符的形式）和分页参数以及查询sql的查询参数得到Page对象
	 * Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param sql
	 *            查询sql
	 * @param countSql
	 *            查询总数的sql
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @param args
	 *            包含sql中?号占位符的参数数组,如果没有参数则传递null
	 * @return Page对象里面的result对象是List<Bean>的形式
	 */
	public Page<Bean> queryBeanByPage(String sql, String countSql,
			int pageSize, int curPageNo, List<Object> params);

	/**
	 * 根据查询sql（sql如果包括查询参数采用?号占位符的形式）和分页参数以及查询sql的查询参数得到Page对象
	 * Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param sql
	 *            查询sql
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @param args
	 *            包含sql中?号占位符的参数数组,如果没有参数则传递null
	 * @return Page对象里面的result对象是List<T>的形式
	 */
	public <E> Page<E> queryObjByPage(String sql, int pageSize, int curPageNo,
			List<Object> params, Class<E> clz);

	/**
	 * 根据查询sql（sql如果包括查询参数采用?号占位符的形式）和分页参数以及查询sql的查询参数得到Page对象
	 * Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param sql
	 *            查询sql
	 * @param countSql
	 *            查询总数的sql
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @param args
	 *            包含sql中?号占位符的参数数组,如果没有参数则传递null
	 * @param clz
	 *            要转化成的实体类E的class
	 * @return Page对象里面的result对象是List<T>的形式
	 */
	public <E> Page<E> queryObjByPage(String sql, String countSql,
			int pageSize, int curPageNo, List<Object> params, Class<E> clz);

	/**
	 * 根据查询sql和参数查询总记录数，提供给queryByPage方法调用
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public int queryTotalCount(String sql, Object[] args);
}
