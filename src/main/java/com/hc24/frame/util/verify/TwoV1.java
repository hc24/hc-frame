package com.hc24.frame.util.verify;


import java.awt.image.BufferedImage;


/**
 * 图像二值化实现
 * @author liat6
 * 
 */
public class TwoV1 {

	public BufferedImage filter(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		int[] pixels = ImageHelper.pixels(image);

		for (int i = 0; i < w * h; i++) {
			int[] rgb = ImageHelper.rgb(pixels[i]);
			if (rgb[0] <= 167 && rgb[1] <= 167 && rgb[2] <= 167) {
				pixels[i] = ImageHelper.DEFAULT_FG;
			}
			// if(rgb[0]>160 || rgb[1]>160 || rgb[2]>160){
			// pixels[i] = ImageHelper.DEFAULT_BG;
			// }
		}

		return ImageHelper.image(pixels, w, h);
	}

}