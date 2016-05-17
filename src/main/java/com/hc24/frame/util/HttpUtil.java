package com.hc24.frame.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * 
 * 不实用HttpClient的工具
 * @author hc24
 * 
 */
public class HttpUtil {
	/**
	 * 
	 * 
	 * @param urlString
	 *            http://www.sina.com.cn 合法的url
	 * 
	 * @return 得到Url返回的数�?
	 */
	public static String get(String urlString) {
		Logger log = Logger.getLogger("HttpUtil.get(String url)");
		StringBuffer stringBuffer = new StringBuffer();
		if (urlString.equalsIgnoreCase("")) {
			return null;
		} else if (urlString.toLowerCase().startsWith("http://")) {
		} else {
			return null;
		}

		HttpURLConnection httpConnection = null;
		URL url;
		int code;// 状�?�?200-正常 500-服务器错�?404-找不到资�?
		try {
			url = new URL(urlString);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			// OutputStreamWriter writer = new
			// OutputStreamWriter(httpConnection.getOutputStream());
			// writer.close();
			code = httpConnection.getResponseCode();

			if (code == HttpURLConnection.HTTP_OK) {
				String contentLen = httpConnection.getHeaderField("Content-Length");
				System.out.println(contentLen);
				String strCurrentLine;
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpConnection.getInputStream()));
				while ((strCurrentLine = reader.readLine()) != null) {
					stringBuffer.append(strCurrentLine).append("\n");
				}
				reader.close();

			}
		} catch (Exception e) {
			System.out.println(e);
			return null;
		} finally {
			httpConnection.disconnect();// 关闭连接
		}
		return stringBuffer.toString();
	}

	/**
	 * post
	 * 
	 * @param urlString
	 * 
	 * @param parameters
	 * 
	 * @return
	 */
	public static String post(String urlString, String parameters) {
		Logger log = Logger
				.getLogger("HttpUtil.post(String urlString, String parameters)");
		StringBuffer stringBuffer = new StringBuffer();

		if (urlString.equalsIgnoreCase("")) {
			return null;
		} else if (urlString.toLowerCase().startsWith("http://")) {
		} else {
			return null;
		}

		HttpURLConnection httpConnection = null;
		URL url;
		int code;
		try {
			url = new URL(urlString);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Length", String
					.valueOf(parameters.length()));
			httpConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			/*
			 * PrintWriter printWriter = new
			 * PrintWriter(httpConnection.getOutputStream());
			 * printWriter.print(parameters); printWriter.close();
			 */
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					httpConnection.getOutputStream(), "8859_1");
			outputStreamWriter.write(parameters);
			outputStreamWriter.flush();
			outputStreamWriter.close();

			code = httpConnection.getResponseCode();

			if (code == HttpURLConnection.HTTP_OK) {

				String strCurrentLine;
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpConnection.getInputStream()));
				while ((strCurrentLine = reader.readLine()) != null) {
					stringBuffer.append(strCurrentLine).append("\n");
				}
				reader.close();

			}
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}finally{
			httpConnection.disconnect();//关闭连接
		}
		return stringBuffer.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(get("http://localhost:8080/up_plat/ws/downPack.action?custsn=P028001355&authsn=AU201212060001&upgradeid=1000"));
	}
}