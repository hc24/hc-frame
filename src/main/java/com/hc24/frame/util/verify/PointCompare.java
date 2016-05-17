package com.hc24.frame.util.verify;


import java.util.Comparator;

/**
 * 点数比较器
 * @author liat6
 *
 */
public class PointCompare implements Comparator<VCode> {
	
	public int compare(VCode o0, VCode o1) {
		return o0.getPoint() - o1.getPoint();
	}
}