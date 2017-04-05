package com.infotaf.restapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infotaf.restapi.data.PgManipDao;
import com.infotaf.restapi.model.PgManip;

@Service
public class PgManipService implements IPgManipService{
    
	private static final Logger logger = LoggerFactory.getLogger(PgManipService.class);
	
    @Autowired
    protected PgManipDao pgmanipDao;
    
    @Transactional(readOnly = false)
	public void savePgManips(List<PgManip> pgManips){
    	logger.debug("IN");
    	for (PgManip pgManip : pgManips) {
    		logger.trace("IN - {}", pgManip.getId());
    		pgmanipDao.save(pgManip);
		}
	}
    @Transactional(readOnly = false)
	public void deletePgManips(){
    	logger.debug("IN");
    	pgmanipDao.deleteAll();
	}
}