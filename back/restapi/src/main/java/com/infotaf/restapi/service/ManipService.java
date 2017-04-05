package com.infotaf.restapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infotaf.restapi.data.ManipDao;
import com.infotaf.restapi.model.Manip;

@Service
public class ManipService implements IManipService{
    
	private static final Logger logger = LoggerFactory.getLogger(ManipService.class);
	
    @Autowired
    protected ManipDao manipDao;
    
    @Transactional(readOnly = true)
    public Manip getManipById(int id){
    	logger.debug("IN - id: {}", id);
        return manipDao.get(id);
    }
    @Transactional(readOnly = true)
    public Manip getManipByName(String name){
    	logger.debug("IN - name: {}", name);
        return manipDao.getManip(name);
    }
    @Transactional(readOnly = false)
	public void saveManip(Manip manip){
    	logger.debug("IN - id :{}", manip.getId());
		manipDao.save(manip);
	}
    @Transactional(readOnly = false)
	public void saveManips(List<Manip> manips){
    	logger.debug("IN");
    	for (Manip manip : manips) {
    		logger.trace("{}", manip.getId());
    		manipDao.save(manip);
		}
	}
    @Transactional(readOnly = false)
	public void deleteManips(){
    	logger.debug("IN");
    	manipDao.deleteAll();
	}
}