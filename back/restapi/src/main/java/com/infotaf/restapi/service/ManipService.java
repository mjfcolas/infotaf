package com.infotaf.restapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infotaf.restapi.data.ManipDao;
import com.infotaf.restapi.model.Manip;
import com.infotaf.restapi.model.Pg;

@Service
public class ManipService implements IManipService{
    
    @Autowired
    protected ManipDao manipDao;
    
    @Transactional(readOnly = true)
    public Manip getManipById(int id){
        return manipDao.get(id);
    }
    @Transactional(readOnly = true)
    public Manip getManipByName(String name){
        return manipDao.getManip(name);
    }
    @Transactional(readOnly = false)
	public void saveManip(Manip manip){
		manipDao.save(manip);
	}
    @Transactional(readOnly = false)
	public void saveManips(List<Manip> manips){
    	for (Manip manip : manips) {
    		manipDao.save(manip);
		}
	}
    @Transactional(readOnly = false)
	public void deleteManips(){
    	manipDao.deleteAll();
	}
}