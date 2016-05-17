package com.hc24.frame.util.verify;

import java.awt.image.BufferedImage;

/**
 * 图像Alpha过滤
 * @author liat6
 *
 */
public class AlphaV1 {
	
	public BufferedImage filter(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		int[] pixels = ImageHelper.pixels(image);
		for(int i=0;i<w*h;i++){
			int[] rgb = ImageHelper.rgb(pixels[i]);
			pixels[i] = ImageHelper.toInt(rgb[0], rgb[1], rgb[2]);
		}
		
		return ImageHelper.image(pixels, w,h);
	}

}