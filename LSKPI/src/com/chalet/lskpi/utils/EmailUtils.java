package com.chalet.lskpi.utils;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.MimeMessageHelper;


/**
 * @author Chalet
 * @version 创建时间：2013年12月4日 下午11:01:45
 * 类说明
 */

public class EmailUtils {
	
	private static Logger logger = Logger.getLogger(EmailUtils.class);

	public static void sendMessage(String filePath,String to, String subject, String body) throws Exception{
		try{
			Properties props = new Properties();
			props.put("mail.smtp.host", CustomizedProperty.getContextProperty("mail.smtp.host", "") );
			props.put("mail.smtp.auth", "true");
			
			MyAuthenticator myAuthenticator = new MyAuthenticator(CustomizedProperty.getContextProperty("email_from"), CustomizedProperty.getContextProperty("email_pwd"));
			
			Session session = Session.getDefaultInstance( props, myAuthenticator );
			MimeMessage message = new MimeMessage(session);
			MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
			
			helper.setFrom(CustomizedProperty.getContextProperty("email_from"));
//			helper.setTo(CustomizedProperty.getContextProperty("lskpi_to").split(","));
			helper.setTo(to.split(","));
//			helper.setSubject(new String(CustomizedProperty.getContextProperty("email_subject").getBytes("UTF-8"),"UTF-8") + subject);
//			helper.setText(new String(CustomizedProperty.getContextProperty("email_body").getBytes("UTF-8"),"UTF-8") + body);
			helper.setSubject("令舒KPI报告 " + subject);
			helper.setText("该邮件为令舒KPI邮件系统自动发送，请勿回复。详情数据请见附件 " + body);
			helper.addAttachment(MimeUtility.encodeText(new File(filePath).getName(),"UTF-8","B"), new File(filePath));
			/**
			 * 
			message.setFrom(InternetAddress.parse(CustomizedProperty.getContextProperty("email_from"))[0]);
			message.addRecipients( MimeMessage.RecipientType.TO, InternetAddress.parse( CustomizedProperty.getContextProperty("lskpi_to") ) );
			message.setReplyTo(InternetAddress.parse( CustomizedProperty.getContextProperty("lskpi_to") ));
			// set Subject
			message.setSubject( CustomizedProperty.getContextProperty("email_subject"), "UTF-8" );
			message.setText(CustomizedProperty.getContextProperty("email_body"));
			// set SentDate
			message.setSentDate( new java.util.Date() );
			 */
			Transport.send(message);
			logger.info("message with subject [" + message.getSubject() + "] sent to " + to);
		}catch(Exception e){
			logger.error("Fail to send email",e);
			throw new Exception(e.getMessage());
		}
	}
	
	public static void sendMessage(List<String> filePaths,String to, String subject, String body) throws Exception{
		try{
			Properties props = new Properties();
			props.put("mail.smtp.host", CustomizedProperty.getContextProperty("mail.smtp.host", "") );
			props.put("mail.smtp.auth", "true");
			
			MyAuthenticator myAuthenticator = new MyAuthenticator(CustomizedProperty.getContextProperty("email_from"), CustomizedProperty.getContextProperty("email_pwd"));
			
			Session session = Session.getDefaultInstance( props, myAuthenticator );
			MimeMessage message = new MimeMessage(session);
			MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
			
			helper.setFrom(CustomizedProperty.getContextProperty("email_from"));
//			helper.setTo(CustomizedProperty.getContextProperty("lskpi_to").split(","));
			helper.setTo(to.split(","));
			helper.setSubject("令舒KPI报告" + subject);
			helper.setText("该邮件为令舒KPI邮件系统自动发送，请勿回复。详情数据请见附件" + body);
			for( String filePath : filePaths ){
				helper.addAttachment(MimeUtility.encodeText(new File(filePath).getName(),"UTF-8","B"), new File(filePath));
			}
			/**
			 * 
			message.setFrom(InternetAddress.parse(CustomizedProperty.getContextProperty("email_from"))[0]);
			message.addRecipients( MimeMessage.RecipientType.TO, InternetAddress.parse( CustomizedProperty.getContextProperty("lskpi_to") ) );
			message.setReplyTo(InternetAddress.parse( CustomizedProperty.getContextProperty("lskpi_to") ));
			// set Subject
			message.setSubject( CustomizedProperty.getContextProperty("email_subject"), "UTF-8" );
			message.setText(CustomizedProperty.getContextProperty("email_body"));
			// set SentDate
			message.setSentDate( new java.util.Date() );
			 */
			Transport.send(message);
			logger.info("message with subject [" + message.getSubject() + "] sent to " + to);
		}catch(Exception e){
			logger.error("Fail to send email",e);
			throw new Exception(e.getMessage());
		}
	}
}
