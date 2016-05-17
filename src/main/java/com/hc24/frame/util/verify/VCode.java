package com.hc24.frame.util.verify;


/**
 * 识别结果
 * 
 * @author liat6
 * 
 */
public class VCode{
	private String letter;
	private int x;
	private int y;
	private int width;
	private int height;
	private int point;

	public String getLetter(){
		return letter;
	}

	public void setLetter(String letter){
		this.letter = letter;
	}

	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}

	public int getWidth(){
		return width;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getHeight(){
		return height;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public int getPoint(){
		return point;
	}

	public void setPoint(int point){
		this.point = point;
	}

}
