package com.hc24.frame.spring.dao.mongo;

import java.util.List;

import org.bson.conversions.Bson;

import com.hc24.frame.spring.dao.IBaseTemplate;
import com.hc24.frame.spring.dao.bean.Bean;
import com.hc24.frame.spring.dao.bean.Page;
import com.hc24.frame.spring.exception.DaoException;

/**
 * MongoDB的查询方法接口
 * 
 * @author hc24
 * 
 */
public interface IMongoTemplate extends IBaseTemplate {

	public <T> T queryForObject(Bson filter, Class<T> requiredType)
			throws DaoException;

	public <T> T queryForObject(Bson filter, Bson sort, Class<T> requiredType)
			throws DaoException;

	public <T> List<T> queryObjectList(Bson filter, Class<T> requiredType);

	public <T> List<T> queryObjectList(Bson filter, Bson sort,
			Class<T> requiredType);

	public <T> List<T> queryObjectList(Bson filter, Bson projection, Bson sort,
			Class<T> requiredType);

	public Bean queryForBean(Bson filter, String collectionName)
			throws DaoException;

	public Bean queryForBean(Bson filter, Bson projection, String collectionName)
			throws DaoException;

	public List<Bean> queryBeanList(Bson filter, String collectionName);

	public List<Bean> queryBeanList(Bson filter, Bson sort,
			String collectionName);

	public List<Bean> queryBeanList(Bson filter, Bson projection, Bson sort,
			String collectionName);

	/**
	 * 根据查询bson和分页参数得到Page对象 Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param filter
	 *            查询参数
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @return Page对象里面的result对象是List<Bean>的形式
	 */
	public Page<Bean> queryBeanByPage(Bson filter, int pageSize, int curPageNo,
			String collectionName);

	/**
	 * 根据查询bson,排序bson和分页参数得到Page对象 Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param filter
	 *            查询
	 * @param sort
	 *            排序
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @return Page对象里面的result对象是List<Bean>的形式
	 */
	public Page<Bean> queryBeanByPage(Bson filter, Bson sort, int pageSize,
			int curPageNo, String collectionName);

	/**
	 * 根据查询bson,映射bson,排序bson和分页参数得到Page对象
	 * Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param filter
	 *            查询
	 * @param projection
	 *            映射
	 * @param sort
	 *            排序
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @return Page对象里面的result对象是List<Bean>的形式
	 */
	public Page<Bean> queryBeanByPage(Bson filter, Bson projection, Bson sort,
			int pageSize, int curPageNo, String collectionName);

	/**
	 * 根据查询bson和分页参数得到Page对象 Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param filter
	 *            查询filter
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @return Page对象里面的result对象是List<T>的形式
	 */
	public <E> Page<E> queryObjByPage(Bson filter, int pageSize, int curPageNo,
			Class<E> clz);

	/**
	 * 根据查询bson和分页参数得到Page对象 Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param filter
	 *            查询参数
	 * @param sort
	 *            排序参数
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @param clz
	 *            要转化成的实体类E的class
	 * @return Page对象里面的result对象是List<T>的形式
	 */
	public <E> Page<E> queryObjByPage(Bson filter, Bson sort, int pageSize,
			int curPageNo, Class<E> clz);

	/**
	 * 根据查询bson和分页参数得到Page对象 Page对象包括本页的数据和本页的基本属性（总记录数、分页大小，当前页，总页数）
	 * 
	 * @param filter
	 *            查询参数
	 * @param sort
	 *            排序参数
	 * @param projection
	 *            映射参数
	 * @param pageSize
	 *            分页大小
	 * @param curPageNo
	 *            请求的当前页
	 * @param clz
	 *            要转化成的实体类E的class
	 * @return Page对象里面的result对象是List<T>的形式
	 */
	public <E> Page<E> queryObjByPage(Bson filter, Bson projection, Bson sort,
			int pageSize, int curPageNo, Class<E> clz);

	/**
	 * 根据查询bson查询总记录数，提供给queryByPage方法调用
	 * 
	 * @param bson
	 * @param args
	 * @return
	 */
	public long queryTotalCount(Bson filter, String collectionName);

	/**
	 * 根据条件更新数据
	 * 
	 * @param filter
	 *            条件
	 * @param update
	 *            更新数据
	 * @param collectionName
	 *            集合名
	 */
	public void update(Bson filter, Bson update, String collectionName);
}
