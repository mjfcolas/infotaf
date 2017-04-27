package com.infotaf.restapi.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infotaf.restapi.config.AppConfig;
import com.infotaf.restapi.data.ParamDao;
import com.infotaf.restapi.model.Param;

@Service
public class ParamService implements IParamService{
    
	private static final Logger logger = LoggerFactory.getLogger(ParamService.class);
	
    @Autowired
    protected ParamDao paramDao;
    
    @Transactional(readOnly = false)
    public int saveParam(String key, String value){
    	logger.debug("IN - key : {} value :{}", key, value);
    	return paramDao.saveParam(key, value);
	}
    
    @Transactional(readOnly = true)
    public Param getParam(String key){
    	logger.debug("IN - key : {}", key);
    	return paramDao.getParam(key);
    }
    
    /**
     * Récupération de la date de mise à jour enregistrée en base
     * @return date de mise à jour enregistrée en base
     * @throws ParseException
     */
    @Transactional(readOnly = true)
    public Date getDateFromParam() throws ParseException{
    	String key = AppConfig.configConstants.getProperty("param.key.updatedate");
	    Param updateDateParam = this.getParam(key);
	    String updateDateString = "";
	    if(updateDateParam != null){
	    	updateDateString = updateDateParam.getValue();
	    }
	    Date updateDate = null;
	    if(!updateDateString.equals("")){
		    String dateFormat = (String) AppConfig.prop.get("infotaf.dateformat");
		    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		    updateDate = formatter.parse(updateDateString);
	    }
	    
	    return updateDate;
    }
}