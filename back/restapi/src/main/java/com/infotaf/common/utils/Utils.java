package com.infotaf.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infotaf.common.exceptions.PgFormatException;
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
			
			if(firstCut == null){
				firstCut = 0;
			}
			if(secondCut == null){
				secondCut = 0;
			}
			
			String nums = id.substring(0, firstCut);
			String tbk = id.substring(firstCut, secondCut);
			String proms = id.substring(secondCut);
			logger.debug("fin du parse : nums: {}, tbk: {}, proms: {}", nums, tbk, proms);
			result.put("nums", nums);
			result.put("tbk", tbk);
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
	
	/**
	 * Récupération des pièces jointe d'un mail
	 * @param message Message javamail
	 * @return
	 * @throws Exception
	 */
	public static Map<String, InputStream> getAttachments(Message message) throws Exception {
	    Object content = message.getContent();
	    if (content instanceof String)
	        return null;        

	    if (content instanceof Multipart) {
	        Multipart multipart = (Multipart) content;
	        Map<String, InputStream> result = new HashMap<String, InputStream>();

	        for (int i = 0; i < multipart.getCount(); i++) {
	            result.putAll(getAttachments(multipart.getBodyPart(i)));
	        }
	        return result;

	    }
	    return null;
	}

	/**
	 * Récupération d'une pièce jointe d'un mail
	 * @param part : Corps du mail (Bodypart)
	 * @return
	 * @throws Exception
	 */
	private static Map<String, InputStream> getAttachments(BodyPart part) throws Exception {
		Map<String, InputStream> result = new HashMap<String, InputStream>();
	    Object content = part.getContent();
	    if (content instanceof InputStream || content instanceof String) {
	        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) || StringUtils.isNotBlank(part.getFileName())) {
	            result.put(part.getFileName(), part.getInputStream());
	            return result;
	        } else {
	            return new HashMap<String, InputStream>();
	        }
	    }

	    if (content instanceof Multipart) {
	            Multipart multipart = (Multipart) content;
	            for (int i = 0; i < multipart.getCount(); i++) {
	                BodyPart bodyPart = multipart.getBodyPart(i);
	                result.putAll(getAttachments(bodyPart));
	            }
	    }
	    return result;
	}
}
