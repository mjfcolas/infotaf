package com.infotaf.restapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infotaf.restapi.model.Manip;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.PgManip;

@Service
public class GlobalService implements IGlobalService{
    
	private static final Logger logger = LoggerFactory.getLogger(GlobalService.class);
	
    @Autowired
    protected PgManipService pgManipService;
    @Autowired
    protected ManipService manipService;
    @Autowired
    protected PgService pgService;
    
    @Transactional(readOnly = false)
    public void saveExcel(List<PgManip> pgManips, List<Manip> manips, List<Pg> pgs){
    	logger.debug("IN");
    	pgService.savePgs(pgs);
    	manipService.saveManips(manips);
    	pgManipService.savePgManips(pgManips);
	}
}