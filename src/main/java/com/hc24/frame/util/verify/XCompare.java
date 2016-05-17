package com.hc24.frame.util.verify;


import java.util.Comparator;

/**
 * 识别结果比较器
 * @author liat6
 *
 */
public class XCompare implements Comparator<VCode> {
	
	public int compare(VCode o0, VCode o1) {
		return o0.getX() - o1.getX();
	}
}