package com.hc24.frame.util;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.hc24.frame.util.verify.ImageHelper;
import com.hc24.frame.util.verify.Recognizer;
import com.hc24.frame.util.verify.TwoV1;


/**
 * Http发送请求工具类（使用HttpClient）
 * @author hc24
 *
 */
public class HttpClientUtil {
	private static final Log logger=LogFactory.getLog(HttpClientUtil.class);
	/** 默认发送超时时间 */
	public static final int DEFAULT_TIMEOUT=3000;
	/** 默认的COOKIE */
	public static final String DEFAULT_COOKIE="AlteonP=348731a134873129baeebaa2; Webtrends=125.71.211.112.1372750167935807; JSESSIONID=0000zc4AECWnF6y2YVYaVp6w9KS:161p21spr";
	//创建一个浏览器
	private static HttpClient httpClient = null;
	
	
	/**
	 * 发送Request得到Response，得到Response后用户做自己的处理
	 * @param request
	 * @return
	 */
	public static HttpResponse postRequest(HttpRequestBase request){
		httpClient = new DefaultHttpClient();
		//设置请求超时时间
		httpClient.getParams().setIntParameter("http.socket.timeout", DEFAULT_TIMEOUT);
		try {
			
			System.err.println("-----------http开始请求--------------");
			//设置使用代理IP进行访问
//			HttpHost proxy = new HttpHost("200.75.51.151", 8080); 
//			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,     
//		                proxy);
			
			HttpResponse response = httpClient.execute(request);
			System.err.println("-----------http请求结束--------------");
			return response;
		}catch(Exception e){
			logger.error(e);
		}
//      //木有显式的关闭啊。。。
//		finally{
//			httpClient.getConnectionManager().shutdown();// 关闭连接
//		}
		return null;

	}
	
	/**
	 * 发送POST请求
	 * @param url
	 * @param headMap
	 * @param paramMap
	 * @return
	 */
	public static HttpResponse postHttp(String url,Map<String, String> headMap,Map<String, String> paramMap){
		
		
		HttpPost request = new HttpPost(url);
		//设置请求头信息
		if(headMap!=null){
			Iterator<String> headIt = headMap.keySet().iterator();
			while(headIt.hasNext()){
				String key=headIt.next();
				request.setHeader(key, headMap.get(key));
			}
		}
		//设置请求参数
		if(paramMap!=null){
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			Iterator<String> paramIt=paramMap.keySet().iterator();
			while(paramIt.hasNext()){
				String key=paramIt.next();
				parameters.add(new BasicNameValuePair(key, paramMap.get(key)));
			}
			HttpEntity reqEntity = null;
			try {
				reqEntity = new UrlEncodedFormEntity(parameters, "GBK");
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// form编码
			request.setEntity(reqEntity);
		}		
		
		return postRequest(request);
//		String responseStr=getHtmlContent(request);		
//		return responseStr;
	}
	
	/**
	 * 根据Request对象获得HTML内容
	 * @param request
	 * @return
	 */
	public static String getHtmlContent(HttpRequestBase request){
		return getHtmlContent(postRequest(request));
	}
	
	/**
	 * 根据Response对象获得HTML内容
	 * @param response
	 * @return html源代码
	 */
	public static String getHtmlContent(HttpResponse response){
					// 从response中取出HttpEntity对象
			HttpEntity entity = response.getEntity();
			System.out.println(entity.getContentType());
			System.out.println(entity.getContentLength());
			System.out.println(EntityUtils.getContentCharSet(entity));
			System.out.println(entity.getContentEncoding());

			// 输出响应的所有头信息

			if (response != null) {
				Header headers[] = response.getAllHeaders();				
				int i = 0;
				while (i < headers.length) {
					logger.info(headers[i].getName() + ":  "+ headers[i].getValue());
					i++;
				}

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					try {
						entity = response.getEntity();
						// 将源码流保存在一个byte数组当中，因为可能需要两次用到该流
						byte[] bytes = EntityUtils.toByteArray(entity);						
						String charSet = "";
						// 如果头部Content-Type中包含了编码信息，那么我们可以直接在此处获取
						charSet = EntityUtils.getContentCharSet(entity);
						logger.info("In header: " + charSet);
						// 如果头部中没有，需要
						// 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息

						if (charSet == "") {
							String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";
							Pattern p = Pattern.compile(regEx,
									Pattern.CASE_INSENSITIVE);
							Matcher m = p.matcher(new String(bytes)); // 默认编码转成字符串，因为我们的匹配中无中文，所以串中可能的乱码对我们没有影响
							boolean result = m.find();
							if (m.groupCount() == 1) {
								charSet = m.group(1);
							} else {
								charSet = "";
							}
						}

						logger.info("Last get: " + charSet);
						// 可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
						logger.info("Encoding string is: "
								+ new String(bytes, charSet));
						
						return new String(bytes, charSet);
					} catch (IOException e) {
						logger.error(e);
					}
				}
			}
			
		
		return null;
	}
	
	public String getHttp(){
		return "";
	}
	
	/**
	 * 得到验证码
	 * @return
	 */
	public static String postVerify(String url,Map<String, String> headMap,Map<String, String> paramMap){

		HttpResponse response = postHttp(url, headMap, paramMap);
		//System.out.println(response.getFirstHeader("Cookie").getValue());
		InputStream is=null;
		FileOutputStream fos=null;
		try {
			is = response.getEntity().getContent();
			
			//保存验证码图片
//			fos=new FileOutputStream(new File("d://code.jpg"));
//			byte[] buffer=new byte[1024*2];
//			while(is.read(buffer)>1){
//				fos.write(buffer);
//			}
//			
//			return JOptionPane.showInputDialog("请输入验证码后继续");
			//程序自动诊断
			BufferedImage image = ImageHelper.image(is);
			//ImageIO.write(image,"bmp",new File("D:\\temp\\" + new Date().getTime() + ".bmp"));
			image = new TwoV1().filter(image);
			//ImageIO.write(image,"bmp",new File("D:\\temp\\" + new Date().getTime() + ".bmp"));
			return new Recognizer().recognize(image);
		} catch (Exception e) {
			logger.error(e);
		} finally{
			try {
				fos.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * 每次调用完后得需要手动关闭连接
	 */
	public static void closeClient(){
		httpClient.getConnectionManager().shutdown();// 关闭连接
	}
	
	public static void main(String[] args) {
		//打开登录页面
//		HttpResponse loginRes=HttpUtil.postHttp("http://tv.travelsky.com/tvb2b/login.jsp", null, null);
//		String cookie=loginRes.getFirstHeader("Set-Cookie").getValue();
//		HttpUtil.closeClient();//记得关闭
		
//		//得到验证码
//		Map<String, String> loginHeadMap=new HashMap<String, String>();
//		loginHeadMap.put("Cookie", DEFAULT_COOKIE);
//		String code= HttpUtil.postVerify("http://tv.travelsky.com/tvb2b/VerificationCode.do?"+new Date().getTime(), loginHeadMap, null);
//		HttpUtil.closeClient();//记得关闭
//		
//		//测试登录
//		Map<String, String> headMap=new HashMap<String, String>();
//		//headMap.put("Referer", "http://www.baidu.com");
//		headMap.put("Cookie", DEFAULT_COOKIE);
//		
//		Map<String, String> paramMap=new HashMap<String, String>();
//		paramMap.put("loginname", "tvb999");
//		paramMap.put("loginpass", "111111a");
//		paramMap.put("vrtifycode", code);//输入验证码
//		
//		//验证码
//		//http://tv.travelsky.com/tvb2b/VerificationCode.do?xxx
//		
//		HttpResponse res=HttpUtil.postHttp("http://tv.travelsky.com/tvb2b/Login.do", headMap, paramMap);
//		String resStr=HttpUtil.getHtmlContent(res);
//		HttpUtil.closeClient();//记得关闭
//		if(resStr.contains("B2B系统欢迎您")){
//			System.out.println("恭喜您，登录成功！");
//		}
		
		//开始连接打印插件
//		Map<String, String> printHeadMap=new HashMap<String, String>();
//		printHeadMap.put("Referer", "http://tv.travelsky.com/tvb2b/ReceiptPrintUndo.do");
//		
//		Map<String, String> printParamMap=new HashMap<String, String>();
//		printParamMap.put("office", "LXA888");
//		printParamMap.put("receiptNo", "1119596198");
//		printParamMap.put("sysType", "B");
//		printParamMap.put("ticketNo", "088-2140078422");
//		
//		HttpResponse res2=HttpUtil.postHttp("http://receiptprint.travelsky.com/receiptPrint/previewReceipt.do", printHeadMap, printParamMap);
//		String resStr2=HttpUtil.getHtmlContent(res2);
//		System.out.println(resStr2);
//		
		Map<String, String> printHeadMap=new HashMap<String, String>();
		printHeadMap.put("Referer", "http://receiptprint.travelsky.com/receiptPrint/previewReceipt.do");
		
		Map<String, String> printParamMap=new HashMap<String, String>();
		printParamMap.put("office", "LXA888");
		printParamMap.put("receiptNo", "1119596198");
		printParamMap.put("sysType", "B");
		printParamMap.put("ticketNo", "088-2140078422");
		
		HttpResponse res2=HttpClientUtil.postHttp("http://receiptprint.travelsky.com/receiptPrint/createReceiptCall.do", printHeadMap, printParamMap);
		String resStr2=HttpClientUtil.getHtmlContent(res2);
		System.out.println(resStr2);
	}
}
