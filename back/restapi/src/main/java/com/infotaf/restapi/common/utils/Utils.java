package com.infotaf.restapi.common.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infotaf.restapi.common.exceptions.PgFormatException;
import com.infotaf.restapi.config.AppConfig;

/**
 * Utilitaires utilisés dans l'application
 * @author emmanuel
 *
 */
public class Utils{
	
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	
	/**
	 * Renommage d'un fichier du sytème de fichier en ajoutant la date à la fin
	 * @param srcFile Fichier à renommer
	 * @throws IOException
	 */
	public static void RenameFile(File srcFile) throws IOException{
		logger.debug("IN - srcName: {}", srcFile.getName());
		String oldName = srcFile.getName();
        String dateFormat = (String) AppConfig.prop.get("infotaf.dateformat");
        String currentDate = new SimpleDateFormat(dateFormat).format(new Date());
        String path = srcFile.getPath().substring(0,srcFile.getPath().lastIndexOf(File.separator));
        String newName = path+File.separator+oldName+"."+currentDate;
        logger.debug("newName: {}", newName);
        File tgtFile = new File(newName);
        Files.move(srcFile.toPath(), tgtFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	/**
	 * Méthode permettant de parser un identifiant nums-tbk-proms en une Map
	 * @param id : String au format 16-116Li212
	 * @return Map avec les clés nums, tbk et proms
	 * @throws PgFormatException
	 */
	public static Map<String, String> ParsePg(String id) throws PgFormatException{
		logger.debug("IN");
		try{
			Map<String, String> result = new HashMap<String, String>();
		
			Integer firstCut = null;
			Integer secondCut = null;
			
			for(int i = 0; i <id.length(); i++){
				if(firstCut == null){
					if(!String.valueOf(id.charAt(i)).equals("-") 
							&& !Utils.tryParseInt(String.valueOf(id.charAt(i)))){
						firstCut = new Integer(i);
					}
				}else{
					if(Utils.tryParseInt(String.valueOf(id.charAt(i)))){
						secondCut = new Integer(i);
						break;
					}
				}
			}
			
			String nums = id.substring(0, firstCut);
			String tbk = id.substring(0, firstCut);
			String proms = id.substring(secondCut);
			logger.debug("fin du parse : nums: {}, tbk: {}, proms: {}", nums, tbk, proms);
			result.put("nums", nums);
			result.put("tbk", id.substring(firstCut, secondCut));
			result.put("proms", proms);	
			return result;
		}catch(Exception e){
			throw new PgFormatException();
		}
		
	}
	
	public static boolean tryParseInt(String value) {  
	     try {  
	         Integer.parseInt(value);  
	         return true;  
	      } catch (NumberFormatException e) {  
	         return false;  
	      }  
	}
}
