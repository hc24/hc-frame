package com.hc24.frame.spring.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.hc24.frame.spring.SpringFactory;
import com.hc24.frame.spring.dao.bean.Bean;
import com.hc24.frame.spring.dao.jdbc.BaseJdbcTemplate;
import com.hc24.frame.spring.exception.DaoException;

/**
 * 表元数据（列）缓存
 * @author hc24
 *
 */
public class TableMetaPool {
	private static Map<String, List<String>> pool=new HashMap<String, List<String>>();
	
	
	
	/**
	 * 判断某个表中列是否存在
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public static boolean contains(String tableName,String columnName){
		tableName=StringUtils.upperCase(tableName);
		columnName=StringUtils.upperCase(columnName);
		List<String> list= pool.get(tableName.toUpperCase());
		if(CollectionUtils.isEmpty(list)){
			list=getMetaData(tableName);
			if(CollectionUtils.isEmpty(list))
				throw new DaoException("表"+tableName+"或其列"+columnName+"不存在");
			pool.put(tableName, list);
		}
			
		for(String str:list){
			if(StringUtils.equals(str, columnName)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 从数据库中得到表的Meta数据中的列名集合
	 * @param tableName
	 * @return
	 */
	private static List<String> getMetaData(String tableName){
		tableName=StringUtils.upperCase(tableName);
		BaseJdbcTemplate t= (BaseJdbcTemplate) SpringFactory.getBean("baseJdbcTemplate");
		List<Bean> list= t.queryBeanList(SqlFactory.getMetaSql(tableName), null);
		List<String> columnList=new ArrayList<String>();
		for(Bean bean:list){
			columnList.add(bean.getString("Field")==null?bean.getString("FIELD")
					:bean.getString("Field"));
		}
		return columnList;
		
	}
}
