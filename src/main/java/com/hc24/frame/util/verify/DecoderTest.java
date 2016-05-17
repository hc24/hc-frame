package com.hc24.frame.util.verify;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class DecoderTest{
	public String decode(String url){
		try{
			HttpClient httpClient = new DefaultHttpClient();// 创建一个浏览器
			HttpGet request=new HttpGet(url);
	
			InputStream is = httpClient.execute(request).getEntity().getContent();
			BufferedImage image = ImageHelper.image(is);
			//ImageIO.write(image,"bmp",new File("D:\\temp\\" + new Date().getTime() + ".bmp"));
			image = new TwoV1().filter(image);
			ImageIO.write(image,"bmp",new File("D:\\temp\\" + new Date().getTime() + ".bmp"));
			return new Recognizer().recognize(image);
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		String vCode=new DecoderTest().decode("http://ebuild.travelsky.com/ebtls/GenVerifyCodeImage.servlet?type=1&timer="+new Date().getTime());
		System.out.println("验证码是："+vCode);
	}
}
