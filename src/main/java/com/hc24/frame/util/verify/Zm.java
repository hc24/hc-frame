package com.hc24.frame.util.verify;


import java.awt.image.BufferedImage;

/**
 * 字模
 * 
 * @author 6tail
 * 
 */
public class Zm{

	/** 字符 */
	private String letter;
	/** 字模图像 */
	private BufferedImage image;

	public String getLetter(){
		return letter;
	}

	public void setLetter(String letter){
		this.letter = letter;
	}

	public BufferedImage getImage(){
		return image;
	}

	public void setImage(BufferedImage image){
		this.image = image;
	}

}
