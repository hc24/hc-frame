package com.hc24.frame.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 集合工具类
 * 
 * @author hc24
 * 
 */
public class CollectionsUtil {

	/**
	 * 字典排序
	 * @param list
	 */
	public static void dictSort(List<?> list) {
		Collections.sort(list, new Comparator<Object>() {

			public int compare(Object o1, Object o2) {
				try {
					// 取得比较对象的汉字编码，并将其转换成字符串
					String s1 = new String(o1.toString().getBytes("GB2312"),
							"ISO-8859-1");
					String s2 = new String(o2.toString().getBytes("GB2312"),
							"ISO-8859-1");
					// 运用String类的 compareTo（）方法对两对象进行比较
					return s1.compareTo(s2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}

		});
	}
	
	/**
	 * 整型列表转整型数组
	 * @param list
	 * @return
	 */
	public static int[] convert(List<Integer> list){
		if(list==null)
			return null;
		int[] array=new int[list.size()];
		for(int i=0;i<list.size();i++){
			array[i]=list.get(i);
		}
		return array;
	}
}
