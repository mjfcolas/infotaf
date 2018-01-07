package com.infotaf.restapi.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotaf.common.utils.Utils;
import com.infotaf.emailSender.EmailSender;
import com.infotaf.restapi.config.AppConfig;
import com.infotaf.restapi.model.BusinessStatus;
import com.infotaf.restapi.model.Mail;
import com.infotaf.restapi.model.MailSenderResult;
import com.infotaf.restapi.web.viewModel.PgBase;
import com.infotaf.restapi.web.viewModel.PgComplete;
import com.infotaf.restapi.web.viewModel.forms.RelaunchForm;

@Service
public class AdministrationService {
	
	private static final Logger logger = LoggerFactory.getLogger(AdministrationService.class);
	
	@Autowired
	protected PgService pgService;
	
	public BusinessStatus relaunchPg(RelaunchForm form) throws IOException{
		logger.debug("IN");
		BusinessStatus result = new BusinessStatus();
		
		List<PgComplete> pgList = pgService.getAllPgsWithManips();
		List<PgBase> pgToTreat = new ArrayList<PgBase>();
		
		String message = "";
		if(form.isSimulation()){
			message = AppConfig.messages.getProperty("message.relaunchMessageSimulation");
		}else{
			message = AppConfig.messages.getProperty("message.relaunchMessage");
		}
		
		message = message.replace("{{AMOUNT}}", form.getAmount().toString());
		
		List<Mail> mails = new ArrayList<Mail>();
		
		ClassLoader classLoader = AdministrationService.class.getClassLoader();
		String rawBody = Utils.readInputStream(classLoader.getResourceAsStream("tafMailBody.html"), "UTF-8");
		
		for (PgComplete pg : pgList) {
			//Sélection des PGs dont le montant du est supérieur au seuil fourni
			if(pg.getTotalDu().compareTo(new BigDecimal(form.getAmount())) > 0){
				pgToTreat.add(pg);
				message +="</BR>"
				+ pg.getNums()
				+ pg.getTbk()
				+ pg.getProms()
				+ "-" +
				pg.getLastName() + " " + pg.getFirstName();

				Mail mail = new Mail();
				mail.setObject(AppConfig.mailProp.getProperty("mail.send.subject"));
				
				String formatedBody = rawBody.replace("{{NAME}}", pg.getFirstName());
				formatedBody = formatedBody.replace("{{AMOUNT}}", AppConfig.df.format(pg.getTotalDu()));
				mail.setBody(formatedBody);
						
				if(AppConfig.prop.getProperty("app.debugMode").equals("false")){
					mail.setAddress(pg.getMail());
				}else{
					mail.setAddress("infotaf@li212.fr");
				}
				
				mails.add(mail);
			}
		}
		result.setMessage(message);
		if(!form.isSimulation()){
			MailSenderResult mailSenderResult = EmailSender.sendMail(mails);
			result.setObject(mailSenderResult);
		}
		return result;
	}

}
