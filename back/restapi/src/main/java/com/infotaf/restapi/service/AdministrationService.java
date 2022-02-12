package com.infotaf.restapi.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.infotaf.emailSender.EmailSender;
import com.infotaf.restapi.config.AppConfig;
import com.infotaf.restapi.model.BusinessStatus;
import com.infotaf.restapi.model.Mail;
import com.infotaf.restapi.model.MailSenderResult;
import com.infotaf.restapi.model.Param;
import com.infotaf.restapi.web.viewModel.PgBase;
import com.infotaf.restapi.web.viewModel.PgComplete;
import com.infotaf.restapi.web.viewModel.forms.RelaunchForm;
import com.infotaf.taffilemanager.TafFileManager;

@Service
public class AdministrationService implements IAdministrationService{
	
	private static final Logger logger = LoggerFactory.getLogger(AdministrationService.class);

	@Autowired
	private Environment env;
	@Autowired
	protected PgService pgService;
	@Autowired
	protected ParamService paramService;
	@Autowired
	protected TafFileManager tafFileManager;
	
	public BusinessStatus processTafFile(MultipartFile file){
		
		return tafFileManager.saveFile(file);
	}
	
	public BusinessStatus relaunchPg(RelaunchForm form) throws IOException{
		logger.debug("IN");
		BusinessStatus result = new BusinessStatus();
		
		List<PgComplete> pgList = pgService.getAllPgsWithManips();
		List<PgBase> pgToTreat = new ArrayList<PgBase>();
		
		String message = "";
		if(form.getIsSimulation()){
			message = AppConfig.messages.getProperty("message.relaunchMessageSimulation");
		}else{
			message = AppConfig.messages.getProperty("message.relaunchMessage");
		}
		
		message = message.replace("{{AMOUNT}}", form.getAmount().toString());
		
		List<Mail> mails = new ArrayList<Mail>();
		
		Param param = paramService.getParam("mailtemplate");
		String rawBody = param == null ? "" : param.getValue();
		
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
				mail.setObject(env.getRequiredProperty("mail.send.subject"));
				
				String formatedBody = rawBody.replace("{{NAME}}", pg.getFirstName());
				formatedBody = formatedBody.replace("{{AMOUNT}}", AppConfig.df.format(pg.getTotalDu()));
				mail.setBody(formatedBody);
						
				if(env.getRequiredProperty("app.debugMode").equals("false")){
					mail.setAddress(pg.getMail());
				}else{
					mail.setAddress("infotaf@li212.fr");
				}
				
				mails.add(mail);
			}
		}
		result.setMessage(message);
		if(!form.getIsSimulation()){
			MailSenderResult mailSenderResult = EmailSender.sendMail(mails, env);
			result.setObject(mailSenderResult);
		}
		return result;
	}

}
