package com.infotaf.restapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}