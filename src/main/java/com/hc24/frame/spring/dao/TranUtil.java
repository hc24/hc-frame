package com.hc24.frame.spring.dao;

import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hc24.frame.spring.dao.bean.Bean;
import com.hc24.frame.spring.dao.bean.Page;
import com.hc24.frame.spring.exception.DaoException;
import com.hc24.frame.util.BeanInfoPool;
import com.hc24.frame.util.StringUtil;


/**
 * 转换工具类，用于表，列<-->实体，属性  之间的转换
 * @author hc24
 *
 */
public class TranUtil {
	private static final Log logger=LogFactory.getLog(TranUtil.class);
	
	private final static String SPLIT="_";
	/** 逗号 */
	public final static String DH=",";
	/** 问号 */
	public final static String WH="?";
	/* 缓存对象 */
	private static Map<String,TranUtil> cache=new HashMap<String, TranUtil>();
	
	/** 分隔符，默认为 _ */
	private String split=SPLIT;
	
	private TranUtil(){}
	
	public static TranUtil newInstance(String split){
		TranUtil tu=cache.get(split);
		if(tu==null){
			tu=new TranUtil();
			cache.put(split, tu);
		}
		tu.split=split;
		return tu;
	}
	
	public static TranUtil newInstance(){
		TranUtil tu=cache.get(SPLIT);
		if(tu==null){
			tu=new TranUtil();
			cache.put(SPLIT, tu);
		}
		return tu;
	}
	
	
	/**
	 * 将domain类的get方法转化为列名
	 * 如createDate会转化为CREATE${split}DATE
	 * @param methodName get方法名，如getCreateDate
	 * @return 列名 如CREATE${split}DATE
	 */
	public String tranColumnName(String methodName){
		String fieldName=methodName.substring(3);
		if(StringUtil.isEmpty(this.split))
			return fieldName;
		StringBuilder sb=new StringBuilder();
		int preStart=0;
		for(int i=0;i<fieldName.length();i++){
			
			if(fieldName.charAt(i)>=65&&fieldName.charAt(i)<=90&&i!=0){
				sb.append(fieldName.substring(preStart,i)).append(split);
				preStart=i;
			}
		}
		if(preStart==0)
			return sb.append(fieldName).toString().toUpperCase();
		else
			return sb.append(fieldName.substring(preStart)).toString().toUpperCase();
	}
	
	/**
	 * 根据列名转化为实体类字段名
	 * @param columnName 列名
	 * @return 实体类字段名
	 */
	public String tranPropName(String columnName){
		if(!StringUtil.isEmpty(split)){//有分隔符，就将分隔符去掉，除第一个单词其他单词首字母大写
			String[] words=columnName.split(split);
			StringBuilder sb=new StringBuilder();
			sb.append(words[0].toLowerCase());
			for(int i=1;i<words.length;i++){
				sb.append(words[i].substring(0,1).toUpperCase()+words[i].substring(1).toLowerCase());
			}
			return sb.toString();
		}else{//如果没有分隔符,则就直接返回了
			return columnName;
		}
	}
	
	/**
	 * 将domain类的类名转化为表名
	 * 如com.travelsky.tibetair.domain.ConfigInfoDomain会转化为[${prefix}][${split}]CONFIG_INFO
	 * 其中prefix表示前缀，一般用户表示项目名，如果不提供则不加前缀
	 * @param className 类全名 如com.travelsky.tibetair.domain.ConfigInfoDomain
	 * @return 表名
	 */
	public String tranTableName(String className,String prefix){
		String clsName=className.substring(className.lastIndexOf(".")+1);
		String tempName=tranColumnName("get"+clsName.substring(0, clsName.indexOf("Domain")));
		if(!StringUtil.isEmpty(prefix))
			tempName=prefix+split+tempName;
		return tempName;
	}
	
	public String tranTableName(String className){
		return tranTableName(className, null);
	}
	
	/**
	 * 根据表名获得其主键的字段名，约定主键返回"ID"
	 * @param tableName 表名
	 * @return 主键字段名
	 */
	public String tranTablePkName(String tableName){
//		int infoPosition=tableName.toUpperCase().indexOf("INFO");
//		if(infoPosition<0){
//			return "id_bgt";
//		}
//		return tableName.substring(0, infoPosition)+"Id";
		return "ID";
	}
	
	/**
	 * 根据实体Class获得insert语句
	 * @param cls
	 * @return
	 */
	public String tranInsertSql(Class<?> cls){
		try{
			/* 通过解析T对象得到表名、所有表字段和值 */
			BeanInfo beanInfo= BeanInfoPool.getBeanInfo(cls);
			MethodDescriptor[] mds=beanInfo.getMethodDescriptors();
			
			String tableName=tranTableName(cls.getName(), "");//表名
			List<String> columnNames=new ArrayList<String>();//所有的表的字段
			for(MethodDescriptor md:mds){
				Method m= md.getMethod();
				if(m.getName().startsWith("get")
						&&m.getDeclaringClass().equals(cls)
						&&m.getGenericParameterTypes().length==0){
					//getMethods.add(m);
					columnNames.add(tranColumnName(m.getName()));
					
				}
			}
			return buildInsertSql(tableName,columnNames);
						
			
		}catch(Exception e){
			throw new DaoException(e);
		}
	}
	
	public String buildPageSql(String sql, int pageSize,int curPageNo, List<Object> params){
		//判断数据库类型
		String fmtStr=SqlFactory.getPageSql();
		switch(SqlFactory.db){
			case MYSQL:
				int startNo = Page.getStartOfPage(curPageNo, pageSize);// 起始号(包含)
				params.add(startNo);
				params.add(pageSize);
				return String.format(fmtStr, sql);// 构造查询分页sql
			case ORACLE:
				startNo=Page.getStartOfPage(curPageNo, pageSize)+1;//起始号(包含)
				int endNo=startNo+pageSize;//结束记录号（不包含）
				params.add(endNo);
				params.add(startNo);
				return String.format(fmtStr, sql);//构造查询分页sql
			default:
				break;
		}
		
		return null;
	}
	
	public String buildInsertSql(String tableName,List<String> columnNames){
		/* 开始构建insert sql语句 */
		StringBuilder insertSql=new StringBuilder();
		StringBuilder valuesSql=new StringBuilder();
		insertSql.append("insert into ").append(tableName).append("(");
		valuesSql.append(" values(");
		for(int i=0;i<columnNames.size()-1;i++){
			insertSql.append(columnNames.get(i)).append(DH);
			valuesSql.append(WH).append(DH);
		}
		insertSql.append(columnNames.get(columnNames.size()-1)).append(")");
		valuesSql.append(WH).append(")");
		insertSql.append(valuesSql.toString());
		//System.out.println(insertSql.toString()+columnValues.toString());
		return insertSql.toString();
	}
	
	public String tranUpdateSql(Class<?> cls){
		try{
			/* 通过解析T对象得到表名、所有表字段和值 */
			BeanInfo beanInfo= BeanInfoPool.getBeanInfo(cls);
			MethodDescriptor[] mds=beanInfo.getMethodDescriptors();
			//List<Method> getMethods=new ArrayList<Method>();//所有以get开头&排除父类方法&无参数的Method
			String tableName=tranTableName(cls.getName(), "");//表名
			String pkName=tranTablePkName(tableName);//主键列名
			String idPropName=tranPropName(pkName);//主键属性名
			
			List<String> columnNames=new ArrayList<String>();//所有的表的字段
			String idColumnName=null;
			for(MethodDescriptor md:mds){
				Method m= md.getMethod();
				if(m.getName().startsWith("get")
						&&m.getDeclaringClass().equals(cls)
						&&m.getGenericParameterTypes().length==0){
					if(m.getName().substring(3).equalsIgnoreCase(idPropName)){
						idColumnName=tranColumnName(m.getName());
					}else{
						//getMethods.add(m);
						columnNames.add(tranColumnName(m.getName()));
					}
				}//end if
			}//end for
			if(idColumnName==null)
				throw new DaoException("指定的ID属性"+idPropName+"在"+cls.getName()+"中不存在或没有对应的get方法");
			
			return buildUpdateSql(tableName, columnNames);
			
		}catch(Exception e){
			throw new DaoException(e);
		}
	}
	
	public String buildUpdateSql(String tableName,List<String> columnNames){
		/* 开始构建update sql语句 */
		String pkName=tranTablePkName(tableName);//主键列名
		
		StringBuilder updateSql=new StringBuilder();
		updateSql.append("update ").append(tableName).append(" set "); 
		for(int i=0;i<columnNames.size()-1;i++){
			updateSql.append(columnNames.get(i)).append("=?,");
		}
		updateSql.append(columnNames.get(columnNames.size()-1)).append("=? where ");
		updateSql.append(pkName).append("=?");
		
		return updateSql.toString();
	}
	
	/**
	 * 把Map列表转化为Bean列表
	 * @param list
	 * @return
	 */
	public List<Bean> tranBeanList(List<Map<String, Object>> list){
		List<Bean> beanList=new ArrayList<Bean>();
		for(Map<String, Object> map:list){
			Bean bean=new Bean();
			bean.fromObject(map);
			beanList.add(bean);
		}
		
		return beanList;
	}
	
	public static void main(String[] args) {
		System.out.println(TranUtil.newInstance().tranTableName("com.travelsky.tibetair.domain.AciUserDomain", ""));
		System.out.println(TranUtil.newInstance().tranTablePkName("aci_user"));
		
	}
}
