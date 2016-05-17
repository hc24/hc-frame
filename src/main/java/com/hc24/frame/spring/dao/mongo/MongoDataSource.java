package com.hc24.frame.spring.dao.mongo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.bson.Document;

import com.hc24.frame.spring.exception.DaoException;
import com.hc24.frame.util.StringUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * 封装Mongo的数据库连接池，用于集成Spring<br/>
 * 根据mongo的driver中{@link MongoClient }和{@link MongoClientOptions}实现<br/>
 * 实际上MongoClient本身就是连接池
 * 
 * @author hc24
 * 
 */
public class MongoDataSource {
	
	/** Mongo集合对象缓存 */
	private static Map<String,MongoCollection<Document>> cCache=new HashMap<String, MongoCollection<Document>>();
	
	private Lock lock = new ReentrantLock();// 锁
	
	private MongoClient mongoClient = null;

	private MongoClientOptions.Builder builder=null;
	
	
	/**
	 * db connection url
	 */
	private String url;
	
	/**
	 * db name
	 */
	private String db;
	
	/**
	 * The maximum number of connections allowed per host for this MongoClient
	 * instance. <br/>
	 * Default is 100.
	 */
	private int connectionsPerHost = 100;

	/**
	 * The minimum number of connections per host for this MongoClient instance. <br/>
	 * Default is 0
	 */
	private int minConnectionsPerHost = 0;

	/**
	 * The connection timeout in milliseconds. <br/>
	 * Default is 10000
	 * */
	private int connectTimeout = 10000;

	/**
	 * The socket timeout for connections used for the cluster heartbeat. The
	 * default value is 20,000 milliseconds
	 */
	private int heartbeatSocketTimeout = 20000;

	/**
	 * The heartbeat frequency. This is the frequency that the driver will
	 * attempt to determine the current state of each server in the cluster. The
	 * default value is 10,000 milliseconds.
	 */
	private int heartbeatFrequency = 10000;

	private int localThreshold = 15;

	/**
	 * The maximum idle time of a pooled connection. A zero value indicates no
	 * limit to the idle time. A pooled connection that has exceeded its idle
	 * time will be closed and replaced when necessary by a new connection.
	 */
	private int maxConnectionIdleTime = 0;

	/**
	 * The maximum life time of a pooled connection. A zero value indicates no
	 * limit to the life time. A pooled connection that has exceeded its life
	 * time will be closed and replaced when necessary by a new connection.
	 */
	private int maxConnectionLifeTime = 0;

	/**
	 * The maximum wait time in milliseconds that a thread may wait for a
	 * connection to become available. <br/>
	 * Default is 120,000. A value of 0 means that it will not wait. A negative
	 * value means to wait indefinitely.
	 */
	private int maxWaitTime = 120000;

	/**
	 * The minimum heartbeat frequency. In the event that the driver has to
	 * frequently re-check a server's availability, it will wait at least this
	 * long since the previous check to avoid wasted effort. The default value
	 * is 500 milliseconds.
	 */
	private int minHeartbeatFrequency = 500;

	/**
	 * the server selection timeout in milliseconds, which defines how long the
	 * driver will wait for server selection to succeed before throwing an
	 * exception. <br/>
	 * Default is 30,000. A value of 0 means that it will timeout immediately if
	 * no server is available. A negative value means to wait indefinitely.
	 */
	private int serverSelectionTimeout = 30000;

	/**
	 * The socket timeout in milliseconds. It is used for I/O socket read and
	 * write operations Socket.setSoTimeout(int) <br/>
	 * Default is 0 and means no timeout.
	 */
	private int SocketTimeout = 0;

	/**
	 * This multiplier, multiplied with the connectionsPerHost setting, gives
	 * the maximum number of threads that may be waiting for a connection to
	 * become available from the pool. All further threads will get an exception
	 * right away. For example if connectionsPerHost is 10 and
	 * threadsAllowedToBlockForConnectionMultiplier is 5, then up to 50 threads
	 * can wait for a connection.
	 */
	private int ThreadsAllowedToBlockForConnectionMultiplier = 5;

	public MongoDataSource(){
		builder=MongoClientOptions.builder();
	}
	
	/**
	 * 设置连接串
	 * @param url
	 */
	public void setUrl(String url) {
		if(StringUtil.isEmpty(url)){
			throw new DaoException("url param is null");
		}
		this.url = url;
	}

	/**
	 * 设置连接的数据库
	 * @param db
	 */
	public void setDb(String db) {
		if(StringUtil.isEmpty(db)){
			throw new DaoException("db param is null");
		}
		this.db = db;
	}


	/**
	 * 返回MongoClient
	 * @return
	 */
	public MongoClient getClient() {
		if(mongoClient==null){
			MongoClientURI uri=new MongoClientURI(this.url, builder);
			mongoClient=new MongoClient(uri);
		}
		return mongoClient;
	}
	
	/**
	 * 返回默认MongoDatabase
	 * @return
	 */
	public MongoDatabase getDatabase(){
		return getClient().getDatabase(db);
	}
	
	/**
	 * 返回集合对象
	 * @param collectionName
	 * @return
	 */
	public MongoCollection<Document> getCollection(String collectionName){
		MongoCollection<Document> coll=cCache.get(collectionName);
		lock.lock();
		if(coll==null){
			coll=getDatabase().getCollection(collectionName);
			cCache.put(collectionName, coll);
		}
		lock.unlock();
		return coll;
	}

	public void setConnectionsPerHost(int connectionsPerHost) {
		this.connectionsPerHost = connectionsPerHost;
		builder.connectionsPerHost(connectionsPerHost);
	}

	public void setMinConnectionsPerHost(int minConnectionsPerHost) {
		this.minConnectionsPerHost = minConnectionsPerHost;
		builder.minConnectionsPerHost(minConnectionsPerHost);
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		builder.connectTimeout(connectTimeout);
	}

	public void setHeartbeatSocketTimeout(int heartbeatSocketTimeout) {
		this.heartbeatSocketTimeout = heartbeatSocketTimeout;
		builder.heartbeatSocketTimeout(heartbeatSocketTimeout);
	}

	public void setHeartbeatFrequency(int heartbeatFrequency) {
		this.heartbeatFrequency = heartbeatFrequency;
		builder.heartbeatFrequency(heartbeatFrequency);
	}

	public void setLocalThreshold(int localThreshold) {
		this.localThreshold = localThreshold;
		builder.localThreshold(localThreshold);
	}

	public void setMaxConnectionIdleTime(int maxConnectionIdleTime) {
		this.maxConnectionIdleTime = maxConnectionIdleTime;
		builder.maxConnectionIdleTime(maxConnectionIdleTime);
	}

	public void setMaxConnectionLifeTime(int maxConnectionLifeTime) {
		this.maxConnectionLifeTime = maxConnectionLifeTime;
		builder.maxConnectionLifeTime(maxConnectionLifeTime);
	}

	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
		builder.maxWaitTime(maxWaitTime);
	}

	public void setMinHeartbeatFrequency(int minHeartbeatFrequency) {
		this.minHeartbeatFrequency = minHeartbeatFrequency;
		builder.minHeartbeatFrequency(minHeartbeatFrequency);
	}

	public void setServerSelectionTimeout(int serverSelectionTimeout) {
		this.serverSelectionTimeout = serverSelectionTimeout;
		builder.serverSelectionTimeout(serverSelectionTimeout);
	}

	public void setSocketTimeout(int socketTimeout) {
		SocketTimeout = socketTimeout;
		builder.socketTimeout(socketTimeout);
	}

	public void setThreadsAllowedToBlockForConnectionMultiplier(
			int threadsAllowedToBlockForConnectionMultiplier) {
		ThreadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
		builder.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);
	}

}
