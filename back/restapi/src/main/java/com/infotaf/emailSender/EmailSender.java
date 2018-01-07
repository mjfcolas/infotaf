package com.infotaf.emailSender;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infotaf.common.exceptions.BusinessException;
import com.infotaf.restapi.config.AppConfig;
import com.infotaf.restapi.model.Mail;
import com.infotaf.restapi.model.MailSenderResult;


public class EmailSender {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);
	
	private static Properties props = new Properties();
	private static String authMecanism = AppConfig.mailProp.getProperty("mail.server.authmechanism");
	
	static{

		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", AppConfig.mailProp.getProperty("mail.server.smtp"));
		props.put("mail.smtp.ssl.enable", AppConfig.mailProp.getProperty("mail.server.enablessl"));
		props.put("mail.smtp.starttls.enable", AppConfig.mailProp.getProperty("mail.server.enablestarttls"));
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.auth.mechanisms", authMecanism);
	}
	
	public static MailSenderResult sendMail(List<Mail> mails) {		
		logger.debug("IN");
		
		MailSenderResult result = new MailSenderResult();
		
		try{
			String sender = AppConfig.mailProp.getProperty("mail.send.sender");
			Session session = Session.getInstance(props);
			Transport transport = session.getTransport();
			String server = AppConfig.mailProp.getProperty("mail.server.smtp");
			String user = AppConfig.mailProp.getProperty("mail.server.smtp.user");
			
			String passwordOrToken = "";
			if(authMecanism.equals("XOAUTH2")){
				passwordOrToken = AppConfig.googleToken.getAccessToken();
			    
			}else{
				passwordOrToken = AppConfig.mailProp.getProperty("mail.credentials.smtp.password");
			}
			
			transport.connect(server, user, passwordOrToken);
			
			for (Mail mail : mails) {
				Message message = new MimeMessage(session);
				
				try{
					message.setFrom(new InternetAddress(sender));
					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(mail.getAddress()));
					message.setSubject(mail.getObject());
					message.setText(mail.getBody());
					transport.sendMessage(message,
				            message.getRecipients(Message.RecipientType.TO));
				} catch(Exception e) {
					logger.error("Echec envoi mail : " + mail.getAddress());
					result.getAddressInError().add(mail.getAddress());
					result.setSuccess(false);
				}
			}
			transport.close();

		
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			result.setSuccess(false);
		} catch (MessagingException e) {
			e.printStackTrace();
			result.setSuccess(false);
		} catch (BusinessException e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}

}
