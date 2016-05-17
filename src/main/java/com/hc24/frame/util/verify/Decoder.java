package com.hc24.frame.util.verify;


import java.awt.image.BufferedImage;

public class Decoder implements IVcodeDecoder{

	
	public String decode(BufferedImage img){
		BufferedImage image = new TwoV1().filter(img);
		String s = new Recognizer().recognize(image);
		return s;
	}
}
