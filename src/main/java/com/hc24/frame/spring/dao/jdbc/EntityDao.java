package com.hc24.frame.spring.dao.jdbc;

import java.io.Serializable;
import java.util.List;
import org.springframework.dao.DataAccessException;


/**
 * 泛型Dao的接口，定义每个Dao通用的方法
 * @author hc24
 */
public interface EntityDao<E, PK extends Serializable> {

	public Object getById(PK id) throws DataAccessException;

	public void deleteById(PK id) throws DataAccessException;

	public void deleteById(PK[] id) throws DataAccessException;

	/** 插入数据 */
	public void save(E entity) throws DataAccessException;
	
	/** 批量插入数据 */
	public void save(E[] entity) throws DataAccessException;

	/** 更新数据 */
	public void update(E entity) throws DataAccessException;

	/** 批量更新数据 */
	public void update(E[] entity) throws DataAccessException;
	
	/** 根据id检查是否插入或是更新数据 */
	public void saveOrUpdate(E entity) throws DataAccessException;

	public boolean isUnique(E entity, String uniquePropertyNames)
			throws DataAccessException;

	/** 用于hibernate.flush() 有些dao实现不需要实现此类 */
	public void flush() throws DataAccessException;

	public List<E> findAll() throws DataAccessException;

}
