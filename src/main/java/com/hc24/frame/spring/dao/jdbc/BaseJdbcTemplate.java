package com.hc24.frame.spring.dao.jdbc;

import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.util.ObjectUtils;

import com.hc24.frame.spring.dao.TableMetaPool;
import com.hc24.frame.spring.dao.TranUtil;
import com.hc24.frame.spring.dao.bean.Bean;
import com.hc24.frame.spring.dao.bean.Page;
import com.hc24.frame.spring.exception.DaoException;
import com.hc24.frame.util.BeanInfoPool;

/**
 * <p>基于spring提供的{@link NamedParameterJdbcTemplate}继续封装<p>
 * <p>目前支持<b>MySql</b>和<b>Oracle</b>数据库</p>
 * <ul>
 * 	<li>对提供的实体类的{@link Class}对象
 * 	的转换，queryForObject方法，规则如下：<br/>
 *	<i>列->属性：ADMIN_NAME->adminName</i>
 * 	</li>
 * <li>
 *  	返回{@link Bean}对象，此对象针对于Map的封装，方便开发更加便利的获得属性，
 *  queryForBean方法，规则如下：<br/>
 *  <i>列->KEY:ADMIN_NAME->ADMIN_NAME</i>
 * </li>
 * </ul>
 * @author hc24
 *
 */
public class BaseJdbcTemplate extends NamedParameterJdbcTemplate implements IJdbcTemplate{
	protected final Log logger = LogFactory.getLog(BaseJdbcTemplate.class);
	
	/** 数据库<->java对象工具类，默认_分隔符*/
	private TranUtil tu=TranUtil.newInstance();
	
	public BaseJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	public BaseJdbcTemplate(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}
	
	public BaseJdbcTemplate(JdbcOperations classicJdbcTemplate) {
		super(classicJdbcTemplate);
	}

	public <T> T queryForObject(String sql, Map<String, ?> paramMap,
			Class<T> requiredType) throws DaoException {
		return super.queryForObject(sql, paramMap,
				ParameterizedBeanPropertyRowMapper.newInstance(requiredType));
	}

	public <T> T queryForObject(String sql, Object[] params,
			Class<T> requiredType) throws DaoException {
		return ((ObjectUtils.isEmpty(params)) ? super.getJdbcOperations()
				.queryForObject(
						sql,
						ParameterizedBeanPropertyRowMapper
								.newInstance(requiredType)) : super
				.getJdbcOperations().queryForObject(
						sql,
						getArguments(params),
						ParameterizedBeanPropertyRowMapper
								.newInstance(requiredType)));
	}
	
	public <T> List<T> queryObjectList(String sql,Object[] params,Class<T> requiredType){
		return ((ObjectUtils.isEmpty(params))?
		super.getJdbcOperations().query(sql, ParameterizedBeanPropertyRowMapper
				.newInstance(requiredType)):
		super.getJdbcOperations().query(sql,ParameterizedBeanPropertyRowMapper
				.newInstance(requiredType),params));
	}

	protected Bean queryForBean(String sql, Map<String, ?> paramMap) throws DaoException {
		List<Map<String,Object>> results = super.query(sql,paramMap, new ColumnMapRowMapper());
		Map<String, Object> map= DataAccessUtils.requiredSingleResult(results);
		Bean bean=new Bean();
		bean.fromObject(map);
		return bean;
	}

	public Bean queryForBean(String sql, Object[] params) throws DaoException {
		List<Map<String,Object>> results =((ObjectUtils.isEmpty(params))?
		super.getJdbcOperations().query(sql, new ColumnMapRowMapper())
		:super.getJdbcOperations().query(sql,params, new ColumnMapRowMapper()));
		Map<String, Object> map= DataAccessUtils.requiredSingleResult(results);
		Bean bean=new Bean();
		bean.fromObject(map);
		return bean;
	}
	
	public List<Bean> queryBeanList(String sql,Object[] params){
		List<Map<String,Object>> results =((ObjectUtils.isEmpty(params))?
				super.getJdbcOperations().query(sql, new ColumnMapRowMapper())
				:super.getJdbcOperations().query(sql,params, new ColumnMapRowMapper()));
		return tu.tranBeanList(results);
	}
	
	/**
	 * 根据主键删除单个对象
	 * @param 主键值
	 * @param clazz 实体类型
	 */
	public <T>void deleteById(Object id,Class<T> clazz) throws DaoException {
		String tableName = tu.tranTableName(clazz.getName(), "");// 获得表名
		deleteById(id, tableName);
	}
	
	/**
	 * 根据主键删除
	 * @param id 主键值
	 * @param tableName 表名
	 */
	public void deleteById(Object id,String tableName){
		String pkName = tu.tranTablePkName(tableName);// 获得主键字段名
		/* 开始构建Sql */
		StringBuilder delSql = new StringBuilder();
		delSql.append("delete from ").append(tableName).append(" where ");
		delSql.append(pkName).append("=?");
		getJdbcOperations().update(delSql.toString(), id);
	}

	/**
	 * 批量删除
	 * 
	 * @param ids 批量删除的主键id数组
	 * @param clazz 实体类型
	 * @throws DaoException
	 */
	public <T>void deleteById(Object[] ids,Class<T> clazz) throws DaoException {
		String tableName = tu.tranTableName(clazz.getName(), "");// 获得表名
		deleteById(ids, tableName);
	}
	
	/**
	 * 批量删除
	 * @param ids 批量删除的主键id数组
	 * @param tableName 表名
	 */
	public void deleteById(Object[] ids,String tableName){
		String pkName = tu.tranTablePkName(tableName);// 获得主键字段名
		/* 开始构建Sql */
		StringBuilder delSql = new StringBuilder();
		delSql.append("delete from ").append(tableName).append(" where ");
		delSql.append(pkName).append(" in(");
		for (int i = 0; i < ids.length - 1; i++) {
			delSql.append("?,");
		}
		delSql.append("?)");
		getJdbcOperations().update(delSql.toString(), ids);
	}

	/**
	 * 获取所有的对象
	 * @param clazz 实体类型
	 */
	public <E>List<E> findAll(Class<E> clazz) throws DaoException {
		String tableName = tu.tranTableName(clazz.getName(), "");// 获得表名
		/* 开始构建Sql */
		StringBuilder selSql = new StringBuilder();
		selSql.append("select * from ").append(tableName);
		logger.info(selSql);
		return queryObjectList(selSql.toString(), null, clazz);
	}
	
	/**
	 * 获取所有对象
	 * @param tableName 表名
	 * @return
	 */
	public List<Bean> findAll(String tableName){
		/* 开始构建Sql */
		StringBuilder selSql = new StringBuilder();
		selSql.append("select * from ").append(tableName);
		logger.info(selSql);
		return queryBeanList(selSql.toString(), null);
	}

	

	/**
	 * 根据主键获得实体对象
	 * @param id 主键值
	 * @param clazz 实体类型
	 */
	public <E>E getById(Object id,Class<E> clazz) throws DaoException {
		String tableName = tu.tranTableName(clazz.getName(), "");// 获得表名
		String pkName = tu.tranTablePkName(tableName);// 获得主键字段名
		/* 开始构建Sql */
		StringBuilder selSql = new StringBuilder();
		selSql.append("select * from ").append(tableName).append(" where ");
		selSql.append(pkName).append("=?");
		List<E> list= queryObjectList(selSql.toString(), new Object[]{id}, clazz);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
	
	/**
	 * 根据主键获得Bean对象
	 * @param id 主键
	 * @param tableName 表名
	 * @return
	 */
	public Bean getById(Object id,String tableName){
		String pkName = tu.tranTablePkName(tableName);// 获得主键字段名
		/* 开始构建Sql */
		StringBuilder selSql = new StringBuilder();
		selSql.append("select * from ").append(tableName).append(" where ");
		selSql.append(pkName).append("=?");
		List<Bean> list= queryBeanList(selSql.toString(), new Object[]{id});
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * 查询某个实体类型是否某个属性值在表中数据是否唯一
	 * @param propertyName 属性名
	 * @param propertyValue 属性值
	 * @param clazz 类型
	 * @return 唯一则返回true，否则返回false
	 * @throws DaoException
	 */
	public <E>boolean isUnique(String propertyName,Object propertyValue,Class<E> clazz)
			throws DaoException {
		
		String tableName = tu.tranTableName(clazz.getName(), "");// 获得表名
		return isUnique(propertyName, propertyValue, tableName);
	}
	
	/**
	 * 查询某个实体类型是否某个属性值在表中数据是否唯一
	 * @param propertyName 属性名
	 * @param propertyValue 属性值
	 * @param tableName 表名
	 * @return 唯一则返回true，否则返回false
	 * @throws DaoException
	 */
	public boolean isUnique(String propertyName,Object propertyValue,String tableName){
		String columnName=tu.tranPropName(propertyName);/** */
		
		/* 开始构建Sql */
		StringBuilder selSql = new StringBuilder();
		selSql.append("select * from ").append(tableName);
		selSql.append(" where ").append(columnName).append("=?");
		logger.info(selSql);
		List<Bean> list= queryBeanList(selSql.toString(), new Object[]{propertyValue});
		//只有唯一的数据则返回true
		if(list.size()==1)
			return true;
		return false;
	}
	
	/**
	 * 查询某个实体类型是否某个属性值在表中数据是否唯一
	 * @param propertyName 属性名
	 * @param propertyValue 属性值
	 * @param clazz 类型
	 * @return 唯一则返回true，否则返回false
	 */
	public <E>boolean isExists(String propertyName,Object propertyValue,Class<E> clazz){
		String tableName = tu.tranTableName(clazz.getName(), "");// 获得表名
		return isExists(propertyName, propertyValue, tableName);
	}
	
	/**
	 * 查询某个实体类型是否某个属性值在表中是否有数据
	 * @param propertyName
	 * @param propertyValue
	 * @param tableName
	 * @return 有数据则返回true，否则返回false
	 */
	public boolean isExists(String propertyName,Object propertyValue,String tableName){
		String columnName=tu.tranPropName(propertyName);/** */
		
		/* 开始构建Sql */
		StringBuilder selSql = new StringBuilder();
		selSql.append("select * from ").append(tableName);
		selSql.append(" where ").append(columnName).append("=?");
		logger.info(selSql);
		List<Bean> list= queryBeanList(selSql.toString(), new Object[]{propertyValue});
		//只有唯一的数据则返回true
		if(list.size()>0)
			return true;
		return false;
	}
	
	/**
	 * 查询某个实体类型是否某个属性集合在表中是否有数据
	 * @param propertyNames
	 * @param propertyValues
	 * @param tableName
	 * @return 有数据则返回true，否则返回false
	 */
	public boolean isExists(String[] propertyNames,Object[] propertyValues,String tableName){
		String[] columnNames=new String[propertyNames.length];
		for(int i=0;i<propertyNames.length;i++){
			String columnName=tu.tranPropName(propertyNames[i]);
			columnNames[i]=columnName;
		}
		/* 开始构建Sql */
		StringBuilder selSql = new StringBuilder();
		selSql.append("select * from ").append(tableName);
		selSql.append(" where 1=1");
		for(int i=0;i<columnNames.length;i++){
			selSql.append(" and ").append(columnNames[i]).append("=?");
		}
		logger.info(selSql);
		List<Bean> list= queryBeanList(selSql.toString(), propertyValues);
		//只有唯一的数据则返回true
		if(list.size()>0)
			return true;
		return false;
	}

	/**
	 * 保存一个实体
	 * @param entity 实体普通的JavaBean
	 * @throws DaoException
	 */
	public void save(Object entity) throws DaoException {
		try {
			
			/* 通过解析T对象得到表名、所有表字段和值 */
			Class<?> cls = entity.getClass();
			String insertSql = tu.tranInsertSql(cls);
			BeanInfo beanInfo= BeanInfoPool.getBeanInfo(cls);
			MethodDescriptor[] mds=beanInfo.getMethodDescriptors();
			
			List<Object> columnValues = new ArrayList<Object>();// 所有表字段值
			for(MethodDescriptor md:mds){
				Method m= md.getMethod();
				if (m.getName().startsWith("get")
						&& m.getDeclaringClass().equals(cls)
						&& m.getGenericParameterTypes().length == 0) {
					columnValues.add(m.invoke(entity));
				}
			}

			logger.info("插入sql：" + insertSql.toString()
					+ columnValues.toString());
			getJdbcOperations().update(insertSql.toString(),
					columnValues.toArray());

		} catch (Exception e) {
			logger.error("保存" + entity + "失败", e);
			throw new DaoException(e);
		}
	}
	
	/**
	 * 保存{@link Bean}
	 * @param bean
	 * @param tableName 表名
	 */
	public void save(Bean bean,String tableName){
		List<String> columnNames=new ArrayList<String>();//所有的表的字段
		List<Object> columnValues = new ArrayList<Object>();// 所有表字段值
		Iterator<String> it= bean.keySet().iterator();
		while(it.hasNext()){
			String key=it.next();
			//在表中存在此列的情况下加入insert
			if(TableMetaPool.contains(tableName, key)){
				columnNames.add(key);
				columnValues.add(bean.get(key));
			}
		}
		
		String insertSql=tu.buildInsertSql(tableName, columnNames);
		
		logger.info("插入sql：" + insertSql.toString()
				+ columnValues.toString());
		getJdbcOperations().update(insertSql.toString(),
				columnValues.toArray());
		
	}
	
	
	public void save(Object[] entitys) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	
	public void save(Bean[] beans, String tableName) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	

	/**
	 * 批量更新
	 */
	public void update(final Object[] entitys) throws DaoException {
		try {
			/* 通过解析T对象得到表名、所有表字段和值 */
			final Class<?> cls = entitys[0].getClass();
			String insertSql = tu.tranUpdateSql(cls);
			logger.info("插入sql：" + insertSql);

			getJdbcOperations().batchUpdate(insertSql,
					new BatchPreparedStatementSetter() {

						
						public void setValues(PreparedStatement ps, int index)
								throws SQLException {
							BeanInfo beanInfo= BeanInfoPool.getBeanInfo(cls);
							MethodDescriptor[] mds=beanInfo.getMethodDescriptors();
							// List<Method> getMethods=new
							// ArrayList<Method>();//所有以get开头&排除父类方法&无参数的Method
							Object id=null;
							List<Object> columnValues = new ArrayList<Object>();// 所有表字段值
							for(MethodDescriptor md:mds){
								Method m= md.getMethod();
								if (m.getName().startsWith("get")
										&& m.getDeclaringClass().equals(cls)
										&& m.getGenericParameterTypes().length == 0) {
									// getMethods.add(m);

									try {
										if(m.getName().equals("getID")){
											id=m.invoke(entitys[index]);
										}else{
											columnValues.add(m
												.invoke(entitys[index]));
										}
									} catch (Exception e) {
										columnValues.add(null);
									}
								}
							}

							for (int i = 0; i < columnValues.size(); i++) {
								StatementCreatorUtils.setParameterValue(ps, i+1, SqlTypeValue.TYPE_UNKNOWN, columnValues.get(i));
								//ps.setObject(i + 1, columnValues.get(i));
							}
							StatementCreatorUtils.setParameterValue(ps, columnValues.size()+1, SqlTypeValue.TYPE_UNKNOWN, id);
							
							logger.info("[" + columnValues.toString() + "]");
						}

						
						public int getBatchSize() {
							return entitys.length;
						}
					});

		} catch (Exception e) {
			logger.error("保存" + ArrayUtils.toString(entitys) + "失败", e);
			throw new DaoException(e);
		}
	}
	
	/**
	 * 批量更新
	 * <br/>
	 * 注意：<i style="color:red;">Bean数组里面的key必须相同才可以同步更新</i>
	 * @param beans
	 * @param tableName
	 */
	public void update(final Bean[] beans,final String tableName){
		
		List<String> columnNames=new ArrayList<String>();//所有的表的字段
		Iterator<String> it= beans[0].keySet().iterator();
		while(it.hasNext()){
			String key=it.next();
			if(TableMetaPool.contains(tableName, key)){
				columnNames.add(key);
			}
		}
		
		String insertSql=tu.buildUpdateSql(tableName, columnNames);
		logger.info("插入sql：" + insertSql);
		
		getJdbcOperations().batchUpdate(insertSql,
				new BatchPreparedStatementSetter() {

					
					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						
						Object id=null;
						List<Object> columnValues = new ArrayList<Object>();// 所有表字段值
						Iterator<String> it= beans[index].keySet().iterator();
						while(it.hasNext()){
							String key=it.next();
							if(TableMetaPool.contains(tableName, key)){
								if("ID".equals(key)){
									id=beans[index].get(key);
								}else{
									columnValues.add(beans[index].get(key));
								}
							}
						}

						for (int i = 0; i < columnValues.size(); i++) {
							StatementCreatorUtils.setParameterValue(ps, i+1, SqlTypeValue.TYPE_UNKNOWN, columnValues.get(i));
							//ps.setObject(i + 1, columnValues.get(i));
						}
						StatementCreatorUtils.setParameterValue(ps, columnValues.size()+1, SqlTypeValue.TYPE_UNKNOWN,id);

						logger.info("[" + columnValues.toString() + "]");
						
					}

					
					public int getBatchSize() {
						return beans.length;
					}
			
		});
	}

	/**
	 * 执行sql（such as an insert, update or delete statement）
	 * @param sql sql
	 * @param args 参数
	 * @return
	 */
	public int update(String sql,Object... args){
		return getJdbcOperations().update(sql, args);
	}

	public void update(Object entity) throws DaoException {
		try {
			/* 通过解析T对象得到表名、所有表字段和值 */
			Class<?> cls = entity.getClass();
			String updateSql = tu.tranUpdateSql(cls);

			BeanInfo beanInfo= BeanInfoPool.getBeanInfo(cls);
			MethodDescriptor[] mds=beanInfo.getMethodDescriptors();
			// List<Method> getMethods=new
			// ArrayList<Method>();//所有以get开头&排除父类方法&无参数的Method
			String tableName = tu.tranTableName(cls.getName(), "");// 表名
			String pkName = tu.tranTablePkName(tableName);// 主键列名
			String idPropName = tu.tranPropName(pkName);// 主键属性名
			List<Object> columnValues = new ArrayList<Object>();// 所有表字段值
			Object idColumnValue = null;
			for(MethodDescriptor md:mds){
				Method m= md.getMethod();
				if (m.getName().startsWith("get")
						&& m.getDeclaringClass().equals(cls)
						&& m.getGenericParameterTypes().length == 0) {
					if (m.getName().substring(3).equalsIgnoreCase(idPropName)) {
						idColumnValue = m.invoke(entity);
					} else {
						// getMethods.add(m);
						columnValues.add(m.invoke(entity));
					}
				}// end if
			}// end for
			if (idColumnValue == null)
				throw new DaoException("指定的ID属性" + idPropName + "在"
						+ cls.getName() + "中不存在或没有对应的get方法");
			columnValues.add(idColumnValue);

			logger.info("更新sql：" + updateSql.toString()
					+ columnValues.toString());
			getJdbcOperations().update(updateSql.toString(),
					columnValues.toArray());

		} catch (Exception e) {
			logger.error("更新" + entity + "失败", e);
			throw new DaoException(e);
		}

	}
	
	public void update(Bean bean,String tableName){
		List<String> columnNames=new ArrayList<String>();//所有的表的字段
		List<Object> columnValues = new ArrayList<Object>();// 所有表字段值
		String pkName=tu.tranTablePkName(tableName);
		if( bean.get(pkName)==null)
			throw new DaoException("指定的ID属性" + pkName + "在"
					+ bean + "中不存在");
		
		Iterator<String> it= bean.keySet().iterator();
		while(it.hasNext()){
			String key=it.next();
			if(!TableMetaPool.contains(tableName, key))
				continue;
			
			if(!key.equals(pkName)){
				columnNames.add(key);
				columnValues.add(bean.get(key));
			}
		}
		columnValues.add(bean.get(pkName));
		
		String updateSql=tu.buildUpdateSql(tableName, columnNames);
		
		logger.info("更新sql：" + updateSql.toString()
				+ columnValues.toString());
		getJdbcOperations().update(updateSql.toString(),
				columnValues.toArray());
		
	}
	
	/**
	 * 更新或保存一个对象
	 */
	public void saveOrUpdate(Object entity) throws DaoException {
		Class<?> cls = entity.getClass();
		String tableName = tu.tranTableName(cls.getName(), "");// 表名
		String pkName = tu.tranTablePkName(tableName);// 主键列名
		String idPropName = tu.tranPropName(pkName);// 主键属性名
		
		Method method = BeanInfoPool.getPropertyDescriptor(cls, idPropName)
				.getReadMethod();
		try {
			Object id =  method.invoke(entity);
			Object e = getById(id,cls);
			if (e == null) {
				save(entity);
			} else {
				update(entity);
			}
		} catch (Exception e) {
			logger.error("保存/更新" + entity + "失败", e);
			throw new DaoException(e);
		}
	}
	

	/**
	 * 更新或保存一个对象
	 */
	public void saveOrUpdate(Bean bean,String tableName) throws DaoException {
		String pkName = tu.tranTablePkName(tableName);// 主键列名
		
		try {
			Object id =  bean.get(pkName);
			Object e = getById(id,tableName);
			if (e == null) {
				save(bean,tableName);
			} else {
				update(bean,tableName);
			}
		} catch (Exception e) {
			logger.error("保存/更新" + bean + "失败", e);
			throw new DaoException(e);
		}
	}
	
	/**
	 * 暂未实现。
	 */
	
	public void saveOrUpdate(Object[] entitys) throws DaoException {
		
		
	}

	/**
	 * 
	 * 暂未实现。
	 */
	
	public void saveOrUpdate(Bean[] beans, String tableName)
			throws DaoException {
		
		
	}

	/**
	 * queryMapByPage的重载方法,sql不包含查询参数
	 * 
	 * @param sql
	 * @param pageSize
	 * @param curPageNo
	 * @return
	 */
	public Page<Map<String, Object>> queryMapByPage(String sql,
			int pageSize, int curPageNo) {
		return queryMapByPage(sql, pageSize, curPageNo, null);
	}

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
	public Page<Map<String, Object>> queryMapByPage(String sql,
			int pageSize, int curPageNo, List<Object> params) {
		return queryMapByPage(sql, getTotalCountSql(sql), pageSize, curPageNo,
				params);
	}

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
			String countSql, int pageSize, int curPageNo, List<Object> params) {
		if (params == null)
			params = new ArrayList<Object>();
		int totalCount = queryTotalCount(countSql, params.toArray());// 得到总记录数
		int startNo = Page.getStartOfPage(curPageNo, pageSize);// 起始号(包含)
		
		String querySql = tu.buildPageSql(sql, pageSize, curPageNo, params);// 构造查询分页sql
		List<Map<String, Object>> results = getJdbcOperations().queryForList(
				querySql, params.toArray());

		logger.info("分页sql:" + querySql + "[params:" + params.toString() + "]");
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(results, startNo, totalCount, pageSize);
		return page;
	}

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
	public Page<Bean> queryBeanByPage(String sql, int pageSize, int curPageNo) {
		return queryBeanByPage(sql, pageSize, curPageNo, null);
	}

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
	public Page<Bean> queryBeanByPage(String sql, int pageSize,
			int curPageNo, List<Object> params) {
		return queryBeanByPage(sql, getTotalCountSql(sql), pageSize, curPageNo,
				params);
	}

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
			int pageSize, int curPageNo, List<Object> params) {
		Page<Map<String, Object>> page = queryMapByPage(sql, countSql,
				pageSize, curPageNo, params);
		List<Bean> list = tu.tranBeanList(page.getRows());
		Page<Bean> newPage = new Page<Bean>(list, page.getStart(),
				page.getTotal(), page.getPageSize());
		return newPage;
	}

	

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
	public <E>Page<E> queryObjByPage(String sql, int pageSize, int curPageNo,
			List<Object> params, Class<E> clz) {
		return queryObjByPage(sql, getTotalCountSql(sql), pageSize, curPageNo,
				params, clz);
	}

	

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
	public <E>Page<E> queryObjByPage(String sql, String countSql, int pageSize,
			int curPageNo, List<Object> params, Class<E> clz) {
		if (params == null)
			params = new ArrayList<Object>();
		
		int totalCount = queryTotalCount(countSql, params.toArray());// 得到总记录数
		int startNo = Page.getStartOfPage(curPageNo, pageSize);// 起始号(包含)
		String querySql = tu.buildPageSql(sql, pageSize, curPageNo, params);
		
		List<E> results = queryObjectList(querySql, params.toArray(), clz);

		logger.info("分页sql:" + querySql + "[params:" + params.toString() + "]");
		;
		Page<E> page = new Page<E>(results, startNo, totalCount, pageSize);
		return page;
	}

	private String getTotalCountSql(String sql) {
		String queryCountSql = "";
		if (sql.contains("group by")) {/* 分组的情况 */
			queryCountSql = "select count(*) from (" + sql + ") as t";
		} else {
			int fromPoint = sql.indexOf("from");
			queryCountSql = "select count(*) " + sql.substring(fromPoint);
		}
		return queryCountSql;
	}

	/**
	 * 根据查询sql和参数查询总记录数，提供给queryByPage方法调用
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public int queryTotalCount(String sql, Object[] args) {
		String queryCountSql = sql;
		Number number = getJdbcOperations().queryForObject(sql, args, Integer.class);
		int count= (number != null ? number.intValue() : 0);

		logger.info("总数sql:" + queryCountSql + "[count:" + count + "]");
		return count;
	}
	
	private Object[] getArguments(Object[] varArgs) {
		if ((varArgs.length == 1) && (varArgs[0] instanceof Object[])) {
			return ((Object[]) varArgs[0]);
		}
		return varArgs;
	}

	

}