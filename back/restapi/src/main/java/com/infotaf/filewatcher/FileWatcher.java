package com.infotaf.filewatcher;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.infotaf.restapi.common.utils.Utils;
import com.infotaf.restapi.config.AppConfig;
import com.infotaf.restapi.model.Manip;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.PgManip;
import com.infotaf.restapi.service.GlobalService;
import com.infotaf.restapi.service.ManipService;
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
	private ApplicationContext appContext;//Context Spring pour instancier RawTafFile (scope=prototype)
	
	protected File folder = null;
	private int sleepTime = Integer.parseInt((String) AppConfig.prop.get("filewatcher.period"));
	private RawTafFile rawTafFile;
	
	/**
	 * Boucle infinie (sauf si runFileWatcher passe à false) surveillant un dossier
	 * Temps de pause défini dans le fichier de config
	 * @throws InterruptedException
	 */
	public void checkFiles() throws InterruptedException{
		logger.debug("IN");
		while(runFileWatcher){
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
			Thread.sleep(sleepTime*1000);
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