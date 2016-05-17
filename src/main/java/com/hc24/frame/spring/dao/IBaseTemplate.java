package com.hc24.frame.spring.dao;

import java.util.List;

import com.hc24.frame.spring.dao.bean.Bean;
import com.hc24.frame.spring.exception.DaoException;

/**
 * 数据库增删改方法接口
 * 
 * @author hc24
 * 
 */
public interface IBaseTemplate {

	/**
	 * 根据主键删除单个对象
	 * 
	 * @param 主键值
	 * @param clazz
	 *            实体类型
	 */
	public <T> void deleteById(Object id, Class<T> clazz) throws DaoException;

	/**
	 * 根据主键删除
	 * 
	 * @param id
	 *            主键值
	 * @param tableName
	 *            表名
	 */
	public void deleteById(Object id, String tableName) throws DaoException;

	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            批量删除的主键id数组
	 * @param clazz
	 *            实体类型
	 * @throws DaoException
	 */
	public <T> void deleteById(Object[] ids, Class<T> clazz)
			throws DaoException;

	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            批量删除的主键id数组
	 * @param tableName
	 *            表名
	 */
	public void deleteById(Object[] ids, String tableName);

	/**
	 * 获取所有的对象
	 * 
	 * @param clazz
	 *            实体类型
	 */
	public <E> List<E> findAll(Class<E> clazz) throws DaoException;

	/**
	 * 获取所有对象
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public List<Bean> findAll(String tableName);

	/**
	 * 根据主键获得实体对象
	 * 
	 * @param id
	 *            主键值
	 * @param clazz
	 *            实体类型
	 */
	public <E> E getById(Object id, Class<E> clazz) throws DaoException;

	/**
	 * 根据主键获得Bean对象
	 * 
	 * @param id
	 *            主键
	 * @param tableName
	 *            表名
	 * @return
	 */
	public Bean getById(Object id, String tableName);

	/**
	 * 查询某个实体类型是否某个属性值在表中数据是否唯一
	 * 
	 * @param propertyName
	 *            属性名
	 * @param propertyValue
	 *            属性值
	 * @param clazz
	 *            类型
	 * @return 唯一则返回true，否则返回false，如果数据不存在则返回false
	 * @throws DaoException
	 */
	public <E> boolean isUnique(String propertyName, Object propertyValue,
			Class<E> clazz);

	/**
	 * 查询某个实体类型是否某个属性值在表中数据是否唯一
	 * 
	 * @param propertyName
	 *            属性名
	 * @param propertyValue
	 *            属性值
	 * @param tableName
	 *            表名
	 * @return 唯一则返回true，否则返回false，如果数据不存在则返回false
	 * @throws DaoException
	 */
	public boolean isUnique(String propertyName, Object propertyValue,
			String tableName);

	/**
	 * 查询某个实体类型是否某个属性值在表中数据是否有数据
	 * 
	 * @param propertyName
	 *            属性名
	 * @param propertyValue
	 *            属性值
	 * @param clazz
	 *            类型
	 * @return 有数据则返回true，否则返回false
	 */
	public <E> boolean isExists(String propertyName, Object propertyValue,
			Class<E> clazz);

	/**
	 * 查询某个实体类型是否某个属性值在表中是否有数据
	 * 
	 * @param propertyName
	 * @param propertyValue
	 * @param tableName
	 * @return 有数据则返回true，否则返回false
	 */
	public boolean isExists(String propertyName, Object propertyValue,
			String tableName);

	/**
	 * 查询某个实体类型是否某个属性集合在表中是否有数据
	 * 
	 * @param propertyNames
	 * @param propertyValues
	 * @param tableName
	 * @return 有数据则返回true，否则返回false
	 */
	public boolean isExists(String[] propertyNames, Object[] propertyValues,
			String tableName);

	/**
	 * 保存一个实体
	 * 
	 * @param entity
	 *            实体普通的JavaBean
	 * @throws DaoException
	 */
	public void save(Object entity) throws DaoException;

	/**
	 * 保存{@link Bean}
	 * 
	 * @param bean
	 * @param tableName
	 *            表名
	 */
	public void save(Bean bean, String tableName) throws DaoException;

	/**
	 * 批量保存
	 * 
	 * @param entitys
	 * @throws DaoException
	 */
	public void save(Object[] entitys) throws DaoException;

	/**
	 * 批量保存
	 * 
	 * @param beans
	 * @param tableName
	 * @throws DaoException
	 */
	public void save(Bean[] beans, String tableName) throws DaoException;

	/**
	 * 更新一个对象
	 * @param entity
	 * @throws DaoException
	 */
	public void update(Object entity) throws DaoException;

	/**
	 * 更新一个Bean
	 * @param bean
	 * @param tableName
	 * @throws DaoException
	 */
	public void update(Bean bean, String tableName) throws DaoException;
	
	/**
	 * 批量更新
	 */
	public void update(final Object[] entitys) throws DaoException;

	/**
	 * 批量更新
	 * 注意：<i style="color:red;">Bean数组里面的key必须相同才可以同步更新</i>
	 * 
	 * @param beans
	 * @param tableName
	 */
	public void update(final Bean[] beans, final String tableName)
			throws DaoException;


	/**
	 * 更新或保存一个对象
	 */
	public void saveOrUpdate(Object entity) throws DaoException;

	/**
	 * 更新或保存一个对象
	 */
	public void saveOrUpdate(Bean bean, String tableName) throws DaoException;

	/**
	 * 批量更新或保存一个对象，要根据主键是否有值来决定
	 * @param entitys
	 * @throws DaoException
	 */
	public void saveOrUpdate(Object[] entitys) throws DaoException;
	
	/**
	 * 批量更新或保存一个Bean，要根据主键是否有值来决定
	 * @param beans
	 * @param tableName
	 * @throws DaoException
	 */
	public void saveOrUpdate(Bean[] beans,String tableName) throws DaoException;
}
