package com.infotaf.filewatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.googleToken.GoogleToken;
import com.infotaf.common.utils.Utils;
import com.infotaf.restapi.config.AppConfig;
import com.infotaf.restapi.model.Manip;
import com.infotaf.restapi.model.Param;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.PgManip;
import com.infotaf.restapi.service.GlobalService;
import com.infotaf.restapi.service.ManipService;
import com.infotaf.restapi.service.ParamService;
import com.infotaf.restapi.service.PgManipService;
import com.infotaf.restapi.service.PgService;
import com.opencsv.CSVReader;

/**
 * Classe surveillant un dossier pour mettre à jour la base si un fichier Excel est trouvé
 * @author emmanuel
 *
 */
@Component
public class FileWatcher {
	
	private static final Logger logger = LoggerFactory.getLogger(FileWatcher.class);
	private static boolean runFileWatcher = true;
	

	@Autowired
	PgService pgService;
	@Autowired
	ManipService manipService;
	@Autowired
	PgManipService pgManipService;
	@Autowired
	GlobalService globalService;
	@Autowired
	ParamService paramService;
	@Autowired
	private ApplicationContext appContext;//Context Spring pour instancier RawTafFile (scope=prototype)
	
	protected File folder = null;
	private int sleepTime = Integer.parseInt((String) AppConfig.prop.get("filewatcher.period"));
	private RawTafFile rawTafFile;
	private GoogleToken googleToken;
	
	
	/**
	 * Boucle infinie (sauf si runFileWatcher passe à false) surveillant un dossier et une boite mail
	 * Temps de pause défini dans le fichier de config
	 * @throws InterruptedException
	 */
	public void checkFiles() throws InterruptedException{
		logger.debug("IN");

		//Initialisation de la classe pour récupérer les tokens d'accès
		String oauthClientId = AppConfig.mailProp.getProperty("mail.oautch.clientid");
		String oauthSecret = AppConfig.mailProp.getProperty("mail.oauth.secret");
		String refreshToken = AppConfig.mailProp.getProperty("mail.oauth.refreshtoken");
		googleToken = new GoogleToken(oauthClientId, oauthSecret, refreshToken);
		
		while(runFileWatcher){
			checkFilesInMails();
			checkFilesInFolder();
			Thread.sleep(sleepTime*1000);
		}
	}
	/**
	 * Lecture des mails pour chercher les pièces jointes qui peuvent être importées
	 */
	private void checkFilesInMails(){
		try
		{
			Properties props = new Properties();
	
			props.put("mail.imap.ssl.enable", "true"); // required for Gmail
			props.put("mail.imap.auth.mechanisms", "XOAUTH2");
			
			Session session = Session.getInstance(props);
			Store store = session.getStore("imap");
					  		    
			//Paramètres pour connection avec actualisation du token d'accès si necessaire
			String server = AppConfig.mailProp.getProperty("mail.server.imap");
			String user = AppConfig.mailProp.getProperty("mail.server.user");
			String accessToken = googleToken.getAccessToken();
		    store.connect(server, user, accessToken);

		    //create the folder object and open it
		    Folder emailFolder = store.getFolder("INBOX");
		    emailFolder.open(Folder.READ_ONLY);
	
		    // retrieve the messages from the folder in an array and print it
		    Message[] messages = emailFolder.getMessages();
		    boolean goodMailFound = false;
		    
		    //Récupération de la dernière date de mise à jour
		    Date updateDate = paramService.getDateFromParam();
		    
		    for (int i = messages.length -1; i >= 0; i--) {
		    	Message message = messages[i];
		    	String mailSubject = message.getSubject();
		    	//Seuls les mails avec le bon objet sont traités
		    	if(AppConfig.mailProp.getProperty("mail.app.subject").equals(mailSubject)){
			    	Map<String, InputStream> attachments = Utils.getAttachments(message);
			    	//Seuls les mails avec une pièce jointe et une seule sont traités
			    	if(attachments != null && attachments.size() == 1){
			    		//Seuls les mails dont la date est postérieure à la date de mise à jour sont traités
			    		//Traitement également si pas de date de mise à jour
			    		Date mailDate = message.getSentDate();
			    		if(updateDate == null || (mailDate != null && mailDate.compareTo(updateDate) > 0)){
				    		for(Map.Entry<String, InputStream> entry : attachments.entrySet()) {
				    		    String attachmentName = entry.getKey();
				    		    String extension = (String) AppConfig.prop.get("filewatcher.extension");
				    		    if(attachmentName.endsWith(extension)){
				    		    	logger.info("Found one correct attachment: {}", attachmentName);
				    		    	saveAttachment(entry.getValue(), entry.getKey());
				    		    	goodMailFound = true;
				    		    	break;
				    		    }
				    		}
				    		if(goodMailFound){
				    			break;
				    		}
			    		}
			    	}
		    	}
		    }
		    
		    emailFolder.close(false);
		    store.close();
		    
		} catch (NoSuchProviderException e) {
	       e.printStackTrace();
	    } catch (MessagingException e) {
	       e.printStackTrace();
	    } catch (Exception e) {
	       e.printStackTrace();
	    }
	}
	
	/**
	 * Sauvegarde d'un inputStream dans le dossier défini dans les paramètres
	 * @param fileToSave
	 * @param fileName
	 */
	private void saveAttachment(InputStream fileToSave, String fileName){
		logger.info("Sauvegarde du fichier récupéré");
		String directory = AppConfig.prop.getProperty("filewatcher.directory");
		String tempFilePath = directory+"/"+fileName+".temp";
		OutputStream outputStream = null;
		try{
			outputStream = new FileOutputStream(new File(tempFilePath));
		
			int read = 0;
			byte[] bytes = new byte[1024];
		
			while ((read = fileToSave.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			
			//Renommage du fichier
			logger.info("Renommage du fichier récupéré");
			File fileToRename = new File(tempFilePath);
			File tgtFile = new File(directory+"/"+fileName);
	        Files.move(fileToRename.toPath(), tgtFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileToSave != null) {
				try {
					fileToSave.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	
			}
		}	
	}
	/**
	 * Lecture d'un dossier pour importer les fichiers qui peuvent l'être
	 */
	private void checkFilesInFolder(){
		String directory = (String) AppConfig.prop.get("filewatcher.directory");
		logger.trace("Verification des fichiers : {}", directory);
		folder = new File(directory);
		File[] listOfFiles = folder.listFiles();

		if(listOfFiles != null){
		    for (int i = 0; i < listOfFiles.length; i++) {
		    	if(isFileCorrect(listOfFiles[i])){
		    		processFile(listOfFiles[i]);
		    	}
		    }
		}
	}
	
	/**
	 * Vérification si le fichier trouvé dans le dossier est apte à être parsé
	 * @param fileToCheck
	 * @return
	 */
	private boolean isFileCorrect(File fileToCheck){
		logger.debug("IN - fichier:{}", fileToCheck.getName());
		String extension = (String) AppConfig.prop.get("filewatcher.extension");
		if(fileToCheck != null && !fileToCheck.isDirectory()){
			if(fileToCheck.getName() != null 
					&& fileToCheck.getName().endsWith(extension)){
				logger.debug("Fichier correct");
				return true;
			}
		}
		logger.debug("Fichier non correct");
		return false;
	}
	
	/**
	 * Parse et enregistrement en base d'un fichier
	 * @param fileToProcess
	 * @return
	 */
	private int processFile(File fileToProcess){
		
		logger.debug("IN");
		logger.info("Traitement du fichier {}", fileToProcess.getName());
		
		CSVReader reader = null;
		try {
			//Purge de la base
			pgManipService.deletePgManips();
			pgService.deletePg();
			manipService.deleteManips();
			//Parse du fichier
            reader = new CSVReader(new FileReader(fileToProcess));
            rawTafFile = appContext.getBean(RawTafFile.class, reader);
            //Enregistrement du fichier en base
            logger.info("Enregistrement du fichier");
            List<Pg> pgs = rawTafFile.getPgs();
            List<Manip> manips = rawTafFile.getManips();
            pgService.savePgs(pgs);
            manipService.saveManips(manips);
            List<PgManip> pgManips = rawTafFile.getPgManips();
            reader.close();
            pgManipService.savePgManips(pgManips);
            //Renommage du fichier
            logger.info("Renommage du fichier");
            Utils.RenameFile(fileToProcess);
            //Enregistrement du paramètre
            String dateFormat = (String) AppConfig.prop.get("infotaf.dateformat");
            String keyParam = (String) AppConfig.configConstants.get("param.key.updatedate");
            String currentDate = new SimpleDateFormat(dateFormat).format(new Date());
            logger.info("Date de mise à jour : {}",  currentDate);
            paramService.saveParam(keyParam, currentDate);
        } catch(IOException e){
        	e.printStackTrace();
        	return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
		return 0;
	}

}