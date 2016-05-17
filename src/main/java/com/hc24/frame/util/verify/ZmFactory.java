package com.hc24.frame.util.verify;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * 字模工厂
 * 
 * @author 6tail
 * 
 */
public class ZmFactory{

	private static final String[] LETTER = "0123456789".split("");

	private static final List<Zm> ZM_LIST = new ArrayList<Zm>();
	static{
		try{
			File dir = new File(ZmFactory.class.getResource("").getPath());
			dir = new File(dir,"zm");
			System.out.println(dir);
			for(String s:LETTER){
				if("".equals(s)) continue;
				Zm zm = new Zm();
				zm.setLetter(s);
				zm.setImage(ImageIO.read(new File(dir,s+".gif")));
				ZM_LIST.add(zm);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static final List<Zm> getZms(){
		return ZM_LIST;
	}
	
	public static void main(String[] args) {
		System.out.println("路径:"+ZmFactory.class.getResource("").getPath());
	}
}
