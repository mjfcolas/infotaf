package com.infotaf.emailSender;

import com.googleToken.GoogleToken;
import com.infotaf.common.exceptions.BusinessException;
import com.infotaf.restapi.model.Mail;
import com.infotaf.restapi.model.MailSenderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;


public class EmailSender {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

	public static MailSenderResult sendMail(List<Mail> mails, Environment env) {
		logger.debug("IN");
		String authMecanism = env.getRequiredProperty("mail.server.authmechanism");

		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", env.getRequiredProperty("mail.server.smtp"));
		props.put("mail.smtp.ssl.enable", env.getRequiredProperty("mail.server.enablessl"));
		props.put("mail.smtp.starttls.enable", env.getRequiredProperty("mail.server.enablestarttls"));
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.auth.mechanisms", env.getRequiredProperty("mail.server.authmechanism"));
		
		MailSenderResult result = new MailSenderResult();
		
		try{
			String sender = env.getRequiredProperty("mail.send.sender");
			Session session = Session.getInstance(props);
			Transport transport = session.getTransport();
			String server = env.getRequiredProperty("mail.server.smtp");
			String user = env.getRequiredProperty("mail.server.smtp.user");
			
			String passwordOrToken = "";
			if(authMecanism.equals("XOAUTH2")){
				passwordOrToken = new GoogleToken(env).getAccessToken();
			    
			}else{
				passwordOrToken = env.getRequiredProperty("mail.credentials.smtp.password");
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
