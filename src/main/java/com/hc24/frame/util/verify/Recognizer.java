package com.hc24.frame.util.verify;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 图像识别
 * 
 * @author liat6
 * 
 */
public class Recognizer{

	/** 字模缓存 */
	private static final List<BufferedImage> ZM = new ArrayList<BufferedImage>();
	private static final List<Zm> zmList = Zm2Factory.getZms();
	static{
		// 初始化字模
		for(int i = 0;i < zmList.size();i++){
			BufferedImage b = new AlphaV1().filter(zmList.get(i).getImage());
			int[] p = ImageHelper.pixels(b);
			int x = b.getWidth();
			int y = b.getHeight();
			int mx = 0;
			int my = 0;
			for(int j = 0;j < b.getHeight();j++){
				for(int k = 0;k < b.getWidth();k++){
					if(p[j * b.getWidth() + k] == ImageHelper.DEFAULT_FG){
						if(x > k){
							x = k;
						}
						if(y > j){
							y = j;
						}
						if(mx < k){
							mx = k;
						}
						if(my < j){
							my = j;
						}
					}
				}
			}
			System.out.println("加载字模：" + zmList.get(i).getLetter());
			ZM.add(b.getSubimage(x,y,mx - x + 1,my - y + 1));
		}
	}

	/**
	 * 比较当前图像与字模图像是否一致
	 * 
	 * @param zm 字模图像
	 * @param o 要比较的图像
	 * @return true/false
	 */
	private boolean same(BufferedImage zm,BufferedImage o){
		int[] a = ImageHelper.pixels(zm);
		int[] b = ImageHelper.pixels(o);
		for(int i = 0;i < zm.getHeight();i++){
			for(int j = 0;j < zm.getWidth();j++){
				if(a[i * zm.getWidth() + j] == ImageHelper.DEFAULT_FG && a[i * zm.getWidth() + j] != b[i * zm.getWidth() + j]){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 检查当前识别结果在所有备选结果中横坐标是否有重复
	 * 
	 * @param o 识别结果
	 * @param l 备选结果
	 * @return true/false
	 */
	private boolean isRepeatedX(VCode o,List<VCode> l){
		for(int i = 0;i < l.size();i++){
			VCode zm = l.get(i);
			if(zm.getX() == o.getX() && zm != o){
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取最优识别结果
	 * 
	 * @param o 要比较的结果
	 * @param l 备选结果
	 * @return 最优结果
	 */
	private VCode getZm(VCode o,List<VCode> l){
		VCode d = o;
		for(int i = 0;i < l.size();i++){
			VCode zm = l.get(i);
			if(zm.getX() == d.getX() && zm.getY() < d.getY()){
				d = zm;
			}
		}
		for(int i = 0;i < l.size();i++){
			VCode zm = l.get(i);
			if(zm.getX() == d.getX() && zm.getY() <= d.getY() && zm.getWidth() > d.getWidth()){
				d = zm;
			}
		}
		for(int i = 0;i < l.size();i++){
			VCode zm = l.get(i);
			if(zm.getX() == d.getX() && zm.getY() <= d.getY() && zm.getWidth() >= d.getWidth() && zm.getHeight() > d.getHeight()){
				d = zm;
			}
		}
		for(int i = 0;i < l.size();i++){
			VCode zm = l.get(i);
			if(zm.getX() == d.getX() && zm.getY() <= d.getY() && zm.getWidth() >= d.getWidth() && zm.getHeight() >= d.getHeight() && zm.getPoint() > d.getPoint()){
				d = zm;
			}
		}
		return d;
	}

	/**
	 * 当前识别结果的区域是否被包含在其他备选结果之中
	 * 
	 * @param o 识别结果
	 * @param l 备选结果
	 * @return true/false
	 */
	private boolean isRegion(VCode o,List<VCode> l){
		for(int i = 0;i < l.size();i++){
			VCode zm = l.get(i);
			// System.out.println(zm.getZm()+" "+o.getZm());
			if(zm != o && o.getX() > zm.getX() && o.getX() + o.getWidth() <= zm.getX() + zm.getWidth()){
				// System.out.println(zm.getZm()+" "+o.getZm());
				return true;
			}
		}
		return false;
	}

	/**
	 * 在所有备选结果中筛选出最适合的结果
	 * 
	 * @param l 所有备选结果
	 * @return 最适合的结果
	 */
	private List<VCode> filter(List<VCode> l){
		List<VCode> olist = new ArrayList<VCode>();
		olist.addAll(l);
		List<VCode> list = new ArrayList<VCode>();
		for(int i = 0;i < l.size();i++){
			VCode zm = l.get(i);
			if(!isRepeatedX(zm,l)){
				list.add(zm);
			}
		}
		olist.removeAll(list);
		List<VCode> regionList = new ArrayList<VCode>();
		for(int i = 0;i < list.size();i++){
			VCode o = list.get(i);
			if(isRegion(o,list)){
				// System.out.println(o.getZm());
				regionList.add(o);
			}
		}
		for(int i = 0;i < olist.size();i++){
			VCode o = olist.get(i);
			if(isRegion(o,list)){
				regionList.add(o);
			}
		}

		olist.removeAll(regionList);
		for(int i = 0;i < olist.size();i++){
			VCode zm = olist.get(i);
			VCode d = getZm(zm,olist);
			if(!list.contains(d)){
				list.add(d);
			}

		}
		for(int i = 0;i < list.size();i++){
			VCode o = list.get(i);
			if(isRegion(o,list)){
				// System.out.println(o.getZm());
				regionList.add(o);
			}
		}
		list.removeAll(regionList);
		return list;
	}

	/**
	 * 将识别结果按坐标进行从左到右排序
	 * 
	 * @param l 识别结果
	 */
	private void sort(List<VCode> l){
		Collections.sort(l,new XCompare());
	}

	/**
	 * 图像识别
	 * 
	 * @param image 图像
	 * @return 识别出的字符串
	 */
	public String recognize(BufferedImage image){
		List<VCode> list = new ArrayList<VCode>();
		for(int i = 0;i < ZM.size();i++){
			BufferedImage m = ZM.get(i);
			for(int k = 0;k <= image.getWidth() - m.getWidth();k++){
				for(int j = 0;j <= image.getHeight() - m.getHeight();j++){
					BufferedImage o = image.getSubimage(k,j,m.getWidth(),m.getHeight());
					if(same(m,o)){
						VCode zm = new VCode();
						int point = 0;
						int[] b = ImageHelper.pixels(m);
						for(int n = 0;n < b.length;n++){
							if(b[n] == ImageHelper.DEFAULT_FG){
								point++;
							}
						}
						zm.setPoint(point);
						zm.setX(k);
						zm.setY(j);
						zm.setWidth(m.getWidth());
						zm.setHeight(m.getHeight());
						zm.setLetter(zmList.get(i).getLetter());
						list.add(zm);
					}
				}
			}
		}

		Collections.sort(list,new PointCompare());
		for(int i = 0;i < list.size();i++){
			VCode zm = list.get(i);
			System.out.println(zm.getLetter() + " = {point:" + zm.getPoint() + ",x:" + zm.getX() + ",y:" + zm.getY() + ",width:" + zm.getWidth() + ",height:" + zm.getHeight() + "}");
		}
		System.out.println("------------");

		list = filter(list);
		for(int i = 0;i < list.size();i++){
			VCode zm = list.get(i);
			System.out.println(zm.getLetter() + " = {point:" + zm.getPoint() + ",x:" + zm.getX() + ",y:" + zm.getY() + ",width:" + zm.getWidth() + ",height:" + zm.getHeight() + "}");
		}
		System.out.println("------------");

		sort(list);

		for(int i = 0;i < list.size();i++){
			VCode zm = list.get(i);
			System.out.println(zm.getLetter() + " = {x:" + zm.getX() + ",y:" + zm.getY() + ",width:" + zm.getWidth() + ",height:" + zm.getHeight() + "}");
		}

		StringBuffer s = new StringBuffer();
		for(int i = 0;i < list.size();i++){
			VCode zm = list.get(i);
			s.append(zm.getLetter());
		}

		return s.toString();
	}

}