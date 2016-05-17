package com.hc24.frame.spring.dao.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.hc24.frame.spring.dao.TranUtil;
import com.hc24.frame.spring.dao.bean.Bean;
import com.hc24.frame.spring.dao.bean.Page;
import com.hc24.frame.spring.exception.DaoException;
import com.hc24.frame.util.ObjectUtil;
import com.hc24.frame.util.StringUtil;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.UpdateResult;

/**
 * 基于Mongo3.0+，封装原生Mongo API的CURD操作
 * <p>原生api操作，请参考：
 * <a href="http://mongodb.github.io/mongo-java-driver/3.0/driver/getting-started/quick-tour/">官方教程</a>
 * </p>
 * @author hc24
 *
 */
public class BaseMongoTemplate implements IMongoTemplate {
	
	private static final Log logger=LogFactory.getLog(BaseMongoTemplate.class);

	private MongoDataSource mongoDataSource;

	/** 无分隔符的转换工具 */
	private TranUtil tu = TranUtil.newInstance(null);

	public void setMongoDataSource(MongoDataSource mongoDataSource) {
		this.mongoDataSource = mongoDataSource;
	}

	
	public <T> void deleteById(Object id, Class<T> clazz) throws DaoException {
		String collectionName = tu.tranTableName(clazz.getName());/* 集合名 */
		deleteById(id, collectionName);
	}

	
	public void deleteById(Object id, String collectionName) {
		MongoCollection<Document> mco = mongoDataSource
				.getCollection(collectionName);
		mco.deleteOne(eq("_id", new ObjectId(id.toString())));
	}

	
	public <T> void deleteById(Object[] ids, Class<T> clazz)
			throws DaoException {
		String collectionName = tu.tranTableName(clazz.getName());/* 集合名 */
		deleteById(ids, collectionName);

	}

	
	public void deleteById(Object[] ids, String collectionName) {
		ObjectId[] oids = new ObjectId[ids.length];
		for (int i = 0; i < ids.length; i++) {
			oids[i] = new ObjectId(ids[i].toString());
		}
		MongoCollection<Document> mco = mongoDataSource
				.getCollection(collectionName);
		mco.deleteMany(in("_id", oids));

	}

	
	public <E> List<E> findAll(Class<E> clazz) throws DaoException {
		String collectionName = tu.tranTableName(clazz.getName());/* 集合名 */
		MongoCollection<Document> mco = mongoDataSource
				.getCollection(collectionName);
		MongoCursor<Document> mc = mco.find().iterator();
		return iterator(mc, clazz);
	}

	
	public List<Bean> findAll(String collectionName) {
		MongoCollection<Document> mco = mongoDataSource
				.getCollection(collectionName);
		MongoCursor<Document> mc = mco.find().iterator();
		return iterator(mc);
	}

	
	public <E> E getById(Object id, Class<E> clazz) throws DaoException {
		String collectionName = tu.tranTableName(clazz.getName());/* 集合名 */
		MongoCollection<Document> mco = mongoDataSource
				.getCollection(collectionName);
		Document doc = mco.find(eq("_id", new ObjectId(id.toString()))).first();
		if (doc != null)
			return ObjectUtil.tranMapToObj(doc, clazz);
		return null;
	}

	
	public Bean getById(Object id, String collectionName) {
		MongoCollection<Document> mco = mongoDataSource
				.getCollection(collectionName);
		Document doc = mco.find(eq("_id", new ObjectId(id.toString()))).first();
		if (doc != null)
			return new Bean(doc);
		return null;
	}

	
	public <E> boolean isUnique(String propertyName, Object propertyValue,
			Class<E> clazz) {
		String collectionName=tu.tranTableName(clazz.getName());
		return isUnique(propertyName, propertyValue, collectionName);
	}

	
	public boolean isUnique(String propertyName, Object propertyValue,
			String collectionName) {
		MongoCollection<Document> mco=mongoDataSource.getCollection(collectionName);
		long num=mco.count(eq(propertyName, propertyValue));
		if(num>1)
			return false;
		return true;
	}

	
	public <E> boolean isExists(String propertyName, Object propertyValue,
			Class<E> clazz) {
		String collectionName=tu.tranTableName(clazz.getName());
		return isExists(propertyName, propertyValue, collectionName);
	}

	
	public boolean isExists(String propertyName, Object propertyValue,
			String collectionName) {
		MongoCollection<Document> mco=mongoDataSource.getCollection(collectionName);
		long num=mco.count(eq(propertyName, propertyValue));
		if(num>0)
			return true;
		return false;
	}

	
	public boolean isExists(String[] propertyNames, Object[] propertyValues,
			String collectionName) {
		if(propertyNames==null||propertyNames.length!=propertyValues.length)
			throw new DaoException("param is error");
		MongoCollection<Document> mco=mongoDataSource.getCollection(collectionName);
		Bson[] bsons=new Bson[propertyNames.length];
		for(int i=0;i<propertyNames.length;i++){
			bsons[i]=eq(propertyNames[i], propertyValues[i]);
		}
		Bson filter=and(bsons);
		long num=mco.count(filter);
		if(num>0)
			return true;
		return false;
	}

	
	public void save(Object entity) throws DaoException {
		String collectionName = tu.tranTableName(entity.getClass().getName());/* 集合名 */
		Bean bean=new Bean(entity);
		save(bean, collectionName);
	}

	
	public void save(Bean bean, String collectionName) {
		MongoCollection<Document> mco = mongoDataSource
				.getCollection(collectionName);
		Document doc = new Document(bean);
		mco.insertOne(doc);

	}
	
	
	public void save(Object[] entitys) throws DaoException {
		if (entitys == null || entitys.length == 0)
			return;
		String collectionName=tu.tranTableName(entitys[0].getClass().getName());
		Bean[] beans=new Bean[entitys.length];
		for (int i=0;i<entitys.length;i++) {
			Bean bean=new Bean(entitys[i]);
			beans[i]=bean;
		}
		save(beans, collectionName);
	}

	
	public void save(Bean[] beans, String collectionName) throws DaoException {
		MongoCollection<Document> mco = mongoDataSource
				.getCollection(collectionName);
		List<Document> list=new ArrayList<Document>();
		for(Bean bean:beans){
			list.add(new Document(bean));
		}
		mco.insertMany(list);
	}
	
	
	public void update(Object entity) throws DaoException {
		String collectionName=tu.tranTableName(entity.getClass().getName());
		Bean bean=new Bean(entity);
		update(bean, collectionName);
	}

	
	public void update(Bean bean, String collectionName) {
		MongoCollection<Document> mco= mongoDataSource.getCollection(collectionName);
		String _id=bean.getString("_id");
		if(_id==null)
			throw new DaoException("_id is null");
		bean.remove("_id");
		Document doc=new Document(bean);
		UpdateResult ur= mco.updateOne(eq("_id", new ObjectId(_id)),
				new Document("$set",doc));
		logger.info(collectionName+"更新"+ur.getModifiedCount()+"记录");
	}

	
	public void update(final Object[] entitys) throws DaoException {
		if (entitys == null || entitys.length == 0)
			return;
		String collectionName = tu.tranTableName(entitys[0].getClass()
				.getName());/* 集合名 */
		Bean[] beans=new Bean[entitys.length];
		for (int i=0;i<entitys.length;i++) {
			Bean bean=new Bean(entitys[i]);
			beans[i]=bean;
		}
		update(beans, collectionName);
	}

	
	public void update(final Bean[] beans, String collectionName) throws DaoException {
		MongoCollection<Document> mco = mongoDataSource
				.getCollection(collectionName);
		List<WriteModel<Document>> list=new ArrayList<WriteModel<Document>>();
		for(Bean bean:beans){
			String _id=bean.getString("_id");
			if(_id==null)
				throw new DaoException("_id is null");
			bean.remove("_id");
			Document doc=new Document(bean);
			UpdateOneModel<Document> uom= new UpdateOneModel<Document>(eq("_id", new ObjectId(_id)),
					new Document("$set",doc));
			list.add(uom);
		}
		BulkWriteResult bwr= mco.bulkWrite(list);
		logger.info(collectionName+"更新"+bwr+"条记录");
	}

	

	
	public void saveOrUpdate(Object entity) throws DaoException {
		String collectionName=tu.tranTableName(entity.getClass().getName());
		Bean bean=new Bean(entity);
		saveOrUpdate(bean, collectionName);
	}

	
	public void saveOrUpdate(Bean bean, String collectionName) throws DaoException {
		MongoCollection<Document> mco= mongoDataSource.getCollection(collectionName);
		String _id=bean.getString("_id");
		ObjectId objectId=null;
		if(StringUtil.isEmpty(_id)){
			objectId=new ObjectId();/* 数据库中应该是不存在滴 */
		}else{
			objectId= new ObjectId(_id);
			
		}
		Document doc=new Document(bean);
		
		UpdateOptions uos=new UpdateOptions();
		uos.upsert(true);
		UpdateResult ur= mco.updateOne(eq("_id", objectId), new Document("$set",doc),uos);
		logger.info(collectionName+"更新"+ur.getModifiedCount()+"记录");		
	}

	
	public void saveOrUpdate(Object[] entitys) throws DaoException {
		if (entitys == null || entitys.length == 0)
			return;
		String collectionName=tu.tranTableName(entitys[0].getClass().getName());
		Bean[] beans=new Bean[entitys.length];
		for(int i=0;i<entitys.length;i++){
			beans[i]=new Bean(entitys[i]);
		}
		saveOrUpdate(beans, collectionName);
	}

	
	public void saveOrUpdate(Bean[] beans, String collectionName)
			throws DaoException {
		MongoCollection<Document> mco= mongoDataSource.getCollection(collectionName);
		
		UpdateOptions uo=new UpdateOptions();
		uo.upsert(true);
		List<WriteModel<Document>> list=new ArrayList<WriteModel<Document>>();
		for(Bean bean:beans){
			String _id=bean.getString("_id");
			bean.remove("_id");
			Document doc=new Document(bean);
			WriteModel<Document> wm=null;
			if(StringUtil.isEmpty(_id)){
				wm=new InsertOneModel<Document>(doc);
			}else{
				wm=new UpdateOneModel<Document>(eq("_id", new ObjectId(_id)),
						new Document("$set",doc), uo);
			}
			list.add(wm);
		}
		
		BulkWriteResult bwr= mco.bulkWrite(list);
		logger.info(collectionName+"更新或保存："+bwr.getModifiedCount()+"记录");
		
	}
	
	
	public <T> T queryForObject(Bson filter, Bson projection,Class<T> requiredType) throws DaoException {
		String collectionName=tu.tranTableName(requiredType.getName());/* 集合名 */
		Bean bean= queryForBean(filter, projection, collectionName);
		if(bean!=null)
			return bean.toObject(requiredType);
		return null;
		
	}

	
	public <T> List<T> queryObjectList(Bson filter, Bson projection, Bson sort,
			Class<T> requiredType) {
		String collectionName=tu.tranTableName(requiredType.getName());/* 集合名 */
		List<Bean> beanList=queryBeanList(filter, projection, sort, collectionName);
		List<T> list=new ArrayList<T>();
		for(Bean bean:beanList){
			T t=bean.toObject(requiredType);
			list.add(t);
		}
		return list;
	}

	
	public Bean queryForBean(Bson filter, Bson projection, String collectionName)
			throws DaoException {
		MongoCollection<Document> mco= mongoDataSource.getCollection(collectionName);
		FindIterable<Document> fi=mco.find(filter);
		if(projection!=null)
			fi.projection(projection);
		
		Document doc= fi.first();
		if(doc!=null)
			return new Bean(doc);
		
		return null;
	}
	
	
	public <T> List<T> queryObjectList(Bson filter, Class<T> requiredType) {
		return queryObjectList(filter, null, requiredType);
	}

	
	public <T> List<T> queryObjectList(Bson filter, Bson sort,
			Class<T> requiredType) {
		return queryObjectList(filter, null,sort, requiredType);
	}

	
	public List<Bean> queryBeanList(Bson filter, Bson projection, Bson sort,
			String collectionName) {
		MongoCollection<Document> mco= mongoDataSource.getCollection(collectionName);
		FindIterable<Document> fi=mco.find(filter);
		if(projection!=null)
			fi.projection(projection);
		if(sort!=null)
			fi.sort(sort);
		
		return iterator(fi.iterator());
	}

	
	public Page<Bean> queryBeanByPage(Bson filter, int pageSize, int curPageNo,
			String collectionName) {
		return queryBeanByPage(filter, null, null, pageSize, curPageNo, collectionName);
	}

	
	public Page<Bean> queryBeanByPage(Bson filter, Bson sort, int pageSize,
			int curPageNo, String collectionName) {
		return queryBeanByPage(filter, null, sort, pageSize, curPageNo, collectionName);
	}

	
	public Page<Bean> queryBeanByPage(Bson filter, Bson projection, Bson sort,
			int pageSize, int curPageNo, String collectionName) {
		MongoCollection<Document> mco=mongoDataSource.getCollection(collectionName);
		FindIterable<Document> fi= mco.find(filter);
		if(projection!=null)
			fi.projection(projection);
		if(sort!=null)
			fi.sort(sort);
		int start=Page.getStartOfPage(curPageNo, pageSize);
		fi.skip(start);
		fi.limit(pageSize);
		
		List<Bean> rows=iterator(fi.iterator());
		long total=mco.count(filter);
		Page<Bean> page=new Page<Bean>(rows, start, total, pageSize);
		
		return page;
	}

	
	public <E> Page<E> queryObjByPage(Bson filter, int pageSize, int curPageNo,
			Class<E> clz) {
		return queryObjByPage(filter, null, pageSize, curPageNo, clz);
	}

	
	public <E> Page<E> queryObjByPage(Bson filter, Bson sort, int pageSize,
			int curPageNo, Class<E> clz) {
		return queryObjByPage(filter, null, sort, pageSize, curPageNo, clz);
	}

	
	public <E> Page<E> queryObjByPage(Bson filter, Bson projection, Bson sort,
			int pageSize, int curPageNo, Class<E> clz) {
		String collectionName=tu.tranTableName(clz.getName());
		Page<Bean> beanPage=queryBeanByPage(filter, projection, sort, pageSize, curPageNo, collectionName);
		List<E> rows=new ArrayList<E>();
		for(Bean bean:beanPage.getRows()){
			E e=bean.toObject(clz);
			rows.add(e);
		}
		Page<E> page=new Page<E>(rows, beanPage.getStart(), beanPage.getTotal(), pageSize);
		return page;
	}

	
	public long queryTotalCount(Bson filter, String collectionName) {
		MongoCollection<Document> mco=mongoDataSource.getCollection(collectionName);
		return mco.count(filter);
	}

	

	
	public <T> T queryForObject(Bson filter, Class<T> requiredType)
			throws DaoException {
		return queryForObject(filter, null, requiredType);
	}

	
	public Bean queryForBean(Bson filter, String collectionName)
			throws DaoException {
		return queryForBean(filter, null, collectionName);
	}

	
	public List<Bean> queryBeanList(Bson filter, String collectionName) {
		return queryBeanList(filter, null, null, collectionName);
	}

	
	public List<Bean> queryBeanList(Bson filter, Bson sort,
			String collectionName) {
		return queryBeanList(filter, null, sort, collectionName);
	}
	
	
	private List<Bean> iterator(MongoCursor<Document> mc) {
		List<Bean> list = new ArrayList<Bean>();
		while (mc.hasNext()) {
			Document doc = mc.next();

			Bean bean = new Bean(doc);
			list.add(bean);
		}
		return list;
	}

	private <E> List<E> iterator(MongoCursor<Document> mc, Class<E> clazz) {
		List<E> list = new ArrayList<E>();
		while (mc.hasNext()) {
			Document doc = mc.next();
			E e = ObjectUtil.tranMapToObj(doc, clazz);
			list.add(e);
		}
		return list;
	}

	
	public void update(Bson filter, Bson update,String collectionName) {
		MongoCollection<Document> mco=mongoDataSource.getCollection(collectionName);
		mco.updateMany(filter, update);
	}

	

}
