package com.hc24.frame.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author hc24
 * 
 *         邮件工具类
 */
public class EmailUtils {
	/** 发件人 */
	public static final String FROM = "tvb2c@tibetairlines.com.cn";
	/** 这些属性可以配置在配置文件中 */
	private static final String HOST = "smtp.tibetairlines.com.cn";
	private static final int PORT = 25;
	private static final String AUTH = "true";
	private static final String USERNAME = "tvb2c@tibetairlines.com.cn";
	private static final String PASSWORD = "tvsyz888";
	private static final String SHOW = "西藏航空有限公司";
	private static Properties props = new Properties();// 这里可以从配置文件中读取

	static {
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.auth", AUTH);
	}

	/**
	 * @param reply
	 *            回复地址
	 * @param to
	 *            收信人地址
	 * @param subject
	 *            邮件标题
	 * @param email
	 *            邮件正文
	 */
	public static boolean sendHtmlEmail(String reply, String to,
			String subject, String email, String fileName[]) {

		// 判断是否需要身份认证
		MyAuthenticator authenticator = new MyAuthenticator(USERNAME, PASSWORD);
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(props,
				authenticator);
		// 打印调试信息
		sendMailSession.setDebug(true);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address fromAddr = new InternetAddress(reply, SHOW);
			// 设置邮件消息的发送者
			mailMessage.setFrom(fromAddr);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address toAddr = new InternetAddress(to);
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipient(Message.RecipientType.TO, toAddr);
			// 设置邮件消息的主题
			mailMessage.setSubject(subject);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(email, "text/html; charset=utf-8");
			// 上传文件
			mainPart.addBodyPart(html);
			if (fileName.length > 0) {
				for (String file : fileName) {
					if (file.length()>0) {
						html = new MimeBodyPart();
						DataSource source = new FileDataSource(file);
						html.setDataHandler(new DataHandler(source));
						html.setFileName(file.substring(file.lastIndexOf("/") + 1));
						mainPart.addBodyPart(html);
					}
				}
			}
			mailMessage.setContent(mainPart);
			mailMessage.saveChanges();

			// 发送邮件
			System.out.println("邮件正在发送." + to);
			Transport.send(mailMessage);
			System.out.println("邮件发送成功." + to);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param from
	 *            发信人地址
	 * @param to
	 *            收信人地址
	 * @param subject
	 *            邮件标题
	 * @param email
	 *            邮件正文
	 */
	public static boolean sendSimpleEmail(String from, String to,
			String subject, String email, String fileName[]) {

		// 判断是否需要身份认证
		MyAuthenticator authenticator = new MyAuthenticator(USERNAME, PASSWORD);
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(props,
				authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address fromAddr = new InternetAddress(FROM, SHOW);

			// 设置邮件消息的发送者
			mailMessage.setFrom(fromAddr);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address toAddr = new InternetAddress(to);
			mailMessage.setRecipient(Message.RecipientType.TO, toAddr);
			// 设置邮件消息的主题
			mailMessage.setSubject(subject);
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(email);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			// 上传文件
			if (fileName.length > 0) {
				for (String file : fileName) {
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(file);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(file.substring(file
							.lastIndexOf("/") + 1));
					multipart.addBodyPart(messageBodyPart);
				}
			}
			mailMessage.setContent(multipart);
			mailMessage.saveChanges();

			// 发送邮件
			System.out.println("邮件正在发送." + to);
			Transport.send(mailMessage);
			System.out.println("邮件发送成功." + to);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断Email格式是否正确
	 * 
	 * @author 2008.10.14
	 * @param email
	 *            判断的Email
	 * @return true or false
	 */
	public static boolean isEmail(String email) {
		String regex = "//w+([-+.]//w+)*@//w+([-.]//w+)*//.//w+([-.]//w+)*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		return m.find();
	}

	/**
	 * 获取Email前缀（@之前的字符串）
	 * 
	 * @author 2008.10.29
	 * @param email
	 * @return @之前的字符串
	 */
	public static String getEmailName(String email) {
		if (email == null || email.trim().equals("")) {
			return "";
		} else {
			int index = email.indexOf("@");
			if (index == -1) {
				return email;
			}
			return email.substring(0, index);
		}
	}

	public static class MyAuthenticator extends Authenticator {
		String userName = null;
		String password = null;

		public MyAuthenticator() {
		}

		public MyAuthenticator(String username, String password) {
			this.userName = username;
			this.password = password;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, password);
		}
	}

	public static void main(String[] args) {
		// sendSimpleEmail(EmailUtils.FROM, "21736217@qq.com", "测试邮件主题",
		// "测试邮件内容",
		// new String[] { "C:/1.txt", "c:/1.html" });
		sendHtmlEmail(EmailUtils.FROM, "21736217@qq.com", "恭喜您中了",
				"<span style=\"background-color:#E53333;\">这是西藏航空的新的邮件发送系统哦，"
						+ "收到有木有，amazing！你中奖了哦</span>",
				new String[] { "C:/150720161323001_1.txt" });
	}
}