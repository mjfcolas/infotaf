package com.infotaf.taffilemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.infotaf.common.exceptions.BusinessException;
import com.infotaf.common.utils.Utils;
import com.infotaf.restapi.config.AppConfig;
import com.infotaf.restapi.model.BusinessStatus;
import com.infotaf.restapi.model.Manip;
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
public class TafFileManager {
	
	private static final Logger logger = LoggerFactory.getLogger(TafFileManager.class);
	private static boolean runFileWatcher = true;
	private static boolean lock = false;

	private Environment env;
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
	private int sleepTime;
	private RawTafFile rawTafFile;

	@Autowired
	public TafFileManager(Environment env){
		this.env = env;
		sleepTime = Integer.parseInt(env.getRequiredProperty("filewatcher.period"));
	}

	/**
	 * Boucle infinie (sauf si runFileWatcher passe à false) surveillant un dossier et une boite mail
	 * Temps de pause défini dans le fichier de config
	 * @throws InterruptedException
	 * @throws FileNotFoundException 
	 */
	public void checkFiles() throws InterruptedException{
		logger.debug("IN");
		
		while(runFileWatcher){
			try{
				//Désactivé pour ne laisser que la fonction d'import
				//checkFilesInMails();
				if(!lock){
					checkFilesInFolder();
				}
			}
			catch(IOException e){
				e.printStackTrace();
				logger.error(e.toString());
			}
			Thread.sleep(sleepTime*1000);
		}
	}

	/**
	 * Lecture d'un dossier pour importer les fichiers qui peuvent l'être
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void checkFilesInFolder() throws FileNotFoundException, IOException{
		lock = true;
		String directory = env.getRequiredProperty("filewatcher.directory");
		logger.trace("Verification des fichiers : {}", directory);
		folder = new File(directory);
		File[] listOfFiles = folder.listFiles();

		if(listOfFiles != null){
		    for (int i = 0; i < listOfFiles.length; i++) {
		    	if(isFileCorrect(listOfFiles[i])){
		    		try{
		    			BusinessStatus result = processFile(new FileInputStream(listOfFiles[i]));
		    			Utils.RenameFile(listOfFiles[i], !result.isSuccess(), env.getRequiredProperty("infotaf.dateformat"), env.getRequiredProperty("infotaf.errorsuffix"));
	    			}catch(IOException e){
	    				e.printStackTrace();
	    			}
		    	}
		    }
		}
		lock = false;
	}
	
	private boolean checkExtension(String name){
		String extension = env.getRequiredProperty("filewatcher.extension");
		if(name != null && name.endsWith(extension)){
			return true;
		}
		return false;
	}
	
	/**
	 * Vérification si le fichier trouvé dans le dossier est apte à être parsé
	 * @param fileToCheck
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private boolean isFileCorrect(File fileToCheck) throws FileNotFoundException, IOException{
		logger.debug("IN - fichier:{}", fileToCheck.getName());
		boolean ok = false;
		if(fileToCheck != null && !fileToCheck.isDirectory()){
			InputStream inputToCheck = new FileInputStream(fileToCheck);
			ok = isInputStreamCorrect(inputToCheck, fileToCheck.getName());
			inputToCheck.close();
		}
		logger.debug(!ok ? "Fichier non correct" : "Fichier correct");
		return ok;
	}
	
	/**
	 * Vérifie un inputStream associé à un nom
	 * @param inputToCheck
	 * @param name
	 * @return
	 * @throws IOException
	 */
	private boolean isInputStreamCorrect(InputStream inputToCheck, String name) throws IOException{
		boolean error = true;
		if(checkExtension(name) && isInputText(inputToCheck)){
			error = false;
		}
		return !error;
	}
	/**
	 * Vérifie si un fichier est un fichier texte
	 * @param fileToCheck
	 * @return
	 * @throws IOException 
	 */
	private boolean isInputText(InputStream fileToCheck) throws IOException{
		
		boolean result = Utils.checkPlainText(fileToCheck);
		
		logger.debug(result ? "Fichier correct" : "Fichier non correct");
		return result;
	}
	
	
	/**
	 * Vérifie et sauvegarde un fichier de taf
	 * @param file
	 * @return
	 */
	public BusinessStatus saveFile(MultipartFile file){
		BusinessStatus result = new BusinessStatus();
		try{
			if(!isInputStreamCorrect(file.getInputStream(), file.getName())){
				result.setSuccess(false);
				result.setMessage((String)AppConfig.messages.get("taffile.error"));
			}
			else{
				logger.info("Traitement du fichier {}", file.getName());
				result = processFile(file.getInputStream());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Parse et enregistrement en base d'un fichier
	 * @param fileToProcess
	 * @return
	 * @throws IOException 
	 */
	private BusinessStatus processFile(InputStream fileToProcess){
		
		logger.debug("IN");
		BusinessStatus result = new BusinessStatus();
		
		CSVReader reader = null;
		try {
			//Parse du fichier
            reader = new CSVReader(new InputStreamReader(fileToProcess), ';');
            rawTafFile = appContext.getBean(RawTafFile.class, reader);
            //Récupération des infos
            List<Pg> pgs = rawTafFile.getPgs();
            List<Manip> manips = rawTafFile.getManips();
			//Purge de la base
			pgManipService.deletePgManips();
			manipService.deleteManips();
            //Enregistrement du fichier en base
            logger.info("Enregistrement du fichier");
            pgService.savePgs(pgs);
            manipService.saveManips(manips);
            List<PgManip> pgManips = rawTafFile.getPgManips();
            reader.close();
            pgManipService.savePgManips(pgManips);
            //Enregistrement du paramètre
            String dateFormat = env.getRequiredProperty("infotaf.dateformat");
            String keyParam = env.getRequiredProperty("param.key.updatedate");
            String currentDate = new SimpleDateFormat(dateFormat).format(new Date());
            logger.info("Date de mise à jour : {}",  currentDate);
            paramService.saveParam(keyParam, currentDate);
		} catch(BusinessException e){
			e.printStackTrace();
			logger.error("Erreur d'enregistrement du fichier" + e.getMessage());
			result.setMessage(e.getMessage());
			result.setSuccess(false);
        } catch(IOException e){
        	result.setSuccess(false);
        	result.setMessage(e.getMessage());
        	e.printStackTrace();
        } catch (Exception e) {
        	result.setSuccess(false);
        	result.setMessage(e.getMessage());
            e.printStackTrace();
            logger.error("Erreur d'enregistrement du fichier");
        }
		if(result.isSuccess()){
			result.setMessage((String) AppConfig.messages.get("taffile.success"));
		}
		return result;
	}

}
