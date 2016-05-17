package com.hc24.frame.spring.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hc24.frame.spring.dao.IBaseTemplate;
import com.hc24.frame.spring.dao.bean.Bean;
import com.hc24.frame.spring.dao.jdbc.IJdbcTemplate;
import com.hc24.frame.spring.dao.mongo.IMongoTemplate;

/**
 * 
 * 使用Bean模式的业务抽象类，封装通用的CURD操作，子类需要实现getTable方法，返回默认的表名
 * @author hc24
 */
public abstract class BeanService<PK>{
	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IBaseTemplate baseTemplate;

	/**
	 * 供操作MongoDB数据库调用
	 * @return
	 */
	protected IMongoTemplate getMongoTemplate(){
		return (IMongoTemplate) baseTemplate;
	}
	
	/**
	 * 供操作Sql数据库调用
	 * @return
	 */
	protected IJdbcTemplate getJdbcTemplate(){
		return (IJdbcTemplate) baseTemplate;
	}

	/**
	 * 子类实现，返回默认的表名
	 * @return
	 */
	protected abstract String getTable();
	
	@Transactional(readOnly=true)
	public Bean getById(PK id) {
		return baseTemplate.getById(id, getTable());
	}
	
	@Transactional(readOnly=true)
	public List<Bean> findAll(){
		return baseTemplate.findAll(getTable());
	}
	
	/** 根据id检查是否插入或是更新数据 */
	public void saveOrUpdate(Bean bean){
		baseTemplate.saveOrUpdate(bean,getTable());
	}
	
	/** 插入数据 */
	public void save(Bean bean){
		baseTemplate.save(bean, getTable());
	}
	
	/**
	 * 批量插入数据
	 * @param beans
	 */
	public void save(Bean[] beans){
		baseTemplate.save(beans);
	}
	
	public void deleteById(PK id) {
		baseTemplate.deleteById(id, getTable());
	}
	
	public void deleteById(PK[] ids) {
		baseTemplate.deleteById(ids, getTable());
	}
	
	public void update(Bean bean) {
		baseTemplate.update(bean, getTable());
	}
	
	/**
	 * 批量更新
	 * @param beans 通用对象数据
	 */
	public void update(Bean[] beans){
		baseTemplate.update(beans, getTable());
	}
	
	/**
	 * 查询表中某个列的指定的值是否唯一
	 * @param propertyName 列名
	 * @param propertyValue 值
	 * @return
	 */
	@Transactional(readOnly=true)
	public boolean isUnique(String propertyName,Object propertyValue)  {
		return baseTemplate.isUnique(propertyName, propertyValue, getTable());
	}
	
	/**
	 * 查询表中某个列的是否存在指定的值
	 * @param propertyName 列名
	 * @param propertyValue 值
	 * @return
	 */
	@Transactional(readOnly=true)
	public boolean isExists(String propertyName,Object propertyValue)  {
		return baseTemplate.isExists(propertyName, propertyValue, getTable());
	}
	
	/**
	 * 查询表中某个列集合是否存在指定的值
	 * @param propertyNames 列名集合
	 * @param propertyValues 值集合
	 * @return
	 */
	@Transactional(readOnly=true)
	public boolean isExists(String[] propertyNames,Object[] propertyValues)  {
		return baseTemplate.isExists(propertyNames, propertyValues, getTable());
	}
	
}
