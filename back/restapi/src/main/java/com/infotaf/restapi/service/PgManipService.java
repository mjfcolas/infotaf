package com.infotaf.restapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infotaf.restapi.data.PgManipDao;
import com.infotaf.restapi.model.PgManip;

@Service
public class PgManipService implements IPgManipService{
    
    @Autowired
    protected PgManipDao pgmanipDao;
    
    @Transactional(readOnly = false)
	public void savePgManips(List<PgManip> pgManips){
    	for (PgManip pgManip : pgManips) {
    		pgmanipDao.save(pgManip);
		}
	}
    @Transactional(readOnly = false)
	public void deletePgManips(){
    	pgmanipDao.deleteAll();
	}
}