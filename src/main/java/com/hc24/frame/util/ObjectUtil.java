package com.hc24.frame.util;

import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 对象工具
 * 把对象写到流里的过程是串行化（Serilization）过程，但是在Java程序师圈子里又非常形象地称为“冷冻”或者“腌咸菜（picking）”过程；
 * 而把对象从流中读出来的并行化（Deserialization）过程则叫做“解冻”或者“回鲜(depicking)”过程。应当指出的是，写在流里的是对象的一个拷贝，
 * 而原对象仍然存在于JVM里面，因此“腌成咸菜”的只是对象的一个拷贝，Java咸菜还可以回鲜。
 * 在Java语言里深复制一个对象，常常可以先使对象实现Serializable接口，然后把对象（实际上只是对象的一个拷贝）写到一个流里（腌成咸菜），
 * 再从流里读出来（把咸菜回鲜），便可以重建对象。
 * 
 * @author hc24
 * 
 */
public class ObjectUtil{

    private ObjectUtil(){}

    /**
     * 将对象序列化并压缩编码为字符串
     * 
     * @param o 对象
     * @return 字符串
     * @throws IOException
     */
    public synchronized static String encode(Object o) throws IOException{
        ByteArrayOutputStream bObject = null;
        ByteArrayOutputStream bZip = null;
        ObjectOutputStream outObject = null;
        ZipOutputStream outZip = null;
        try{
        	bObject = new ByteArrayOutputStream();
        	bZip = new ByteArrayOutputStream();
        	outObject = new ObjectOutputStream(bObject);
        	outObject.writeObject(o);
        	outObject.flush();
        	outZip = new ZipOutputStream(bZip);
        	outZip.putNextEntry(new ZipEntry("0"));
        	outZip.write(bObject.toByteArray());
        	outZip.closeEntry();
            return new BASE64Encoder().encode(bZip.toByteArray());
        }catch(Exception e){
        	throw new RuntimeException("序列化对象出错",e);
        }finally{
        	try{
        		outZip.close();
        		bZip.close();
        		outObject.close();
        		bObject.close();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	outZip = null;
        	bZip = null;
        	outObject = null;
        	bObject = null;
        }
    }

    /**
     * 将字符串解压缩并解码转换为对象
     * 
     * @param s 字符串
     * @return 对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public synchronized static Object decode(String s) throws IOException, ClassNotFoundException{
        ByteArrayOutputStream bObject = null;
        ByteArrayInputStream bZip = null;
        ObjectInputStream inObject = null;
        ZipInputStream inZip = null;
        try{
        	bObject = new ByteArrayOutputStream();
        	bZip = new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(s));
        	inZip = new ZipInputStream(bZip);
        	inZip.getNextEntry();
        	byte[] buffer = new byte[1024]; 
        	int offset = -1;
        	while((offset = inZip.read(buffer)) != -1) { 
        		bObject.write(buffer, 0, offset);
        	}
        	inObject = new ObjectInputStream(new ByteArrayInputStream(bObject.toByteArray()));
            return inObject.readObject();
        }catch(Exception e){
        	throw new RuntimeException("反序列化对象出错",e);
        }finally{
        	try{
        		inZip.close();
        		bZip.close();
        		inObject.close();
        		bObject.close();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	inZip = null;
        	bZip = null;
        	inObject = null;
        	bObject = null;
        }
    }
    
    /**
     * 把Map转化成指定对象，把Map中存在的属性的值赋给T，如果不存在，则不做操作
     * @param map
     * @param clazz
     * @return
     */
    public static <T>T  tranMapToObj(Map<String, Object> map,Class<T> cls){
    	/* 通过解析T对象得到表名、所有表字段和值 */
		BeanInfo bi=BeanInfoPool.getBeanInfo(cls);
		MethodDescriptor[]	mds= bi.getMethodDescriptors();
		
		try{
			T t=cls.newInstance();
			for(MethodDescriptor md:mds){
				Method m=md.getMethod();
				if(m.getName().startsWith("set")
						&&m.getDeclaringClass().equals(cls)
						&&m.getGenericParameterTypes().length==1){
					String methodName=m.getName();
					String keyName=methodName.substring(3, 4).toLowerCase()+methodName.substring(4);
					Object value=map.get(keyName);
					try {
					if(value!=null)
						m.invoke(t, map.get(keyName));
					}catch(Exception e){
						e.printStackTrace();
					}				
				}
			}
			return t;
		}catch(Exception e){
			throw new RuntimeException("map->"+cls.getName()+"出错",e);
		}
    }
    
    /**
     * 将JavaBean对象中的get对象转化为Map
     * @param <T>
     * @param t
     * @return
     */
    public static <T> Map<String,Object> tranObjToMap(T t){
    	Map<String, Object> map=new HashMap<String, Object>();
    	BeanInfo bi=BeanInfoPool.getBeanInfo(t.getClass());
		MethodDescriptor[]	mds= bi.getMethodDescriptors();
    	//过滤get方法
		for(MethodDescriptor md:mds){
			Method m=md.getMethod();
			if(m.getName().startsWith("get")
					&&m.getDeclaringClass().equals(t.getClass())
					&&m.getGenericParameterTypes().length==0){
				String key=m.getName().substring(3);
				key=key.substring(0,1).toLowerCase()+key.substring(1);
				try {
					Object val= m.invoke(t);
					map.put(key, val);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
    	return map;
    }
    
    public static void main(String[] args) {
//		CountryInfoDomain c=new CountryInfoDomain();
//		c.setCountryCm("中华人民共和国");
//		System.out.println(c.getCountryCm());
//		
//		Map<String, Object> map=new HashMap<String, Object>();
//		map.put("countryId", "10000");
//		map.put("countryEm", "Cn");
//		map.put("countryCm", "中国");
//		
//		Objecter.tranMapToObj(map, c);
//		System.out.println(c.getCountryCm());
    	
 
		
	}
}