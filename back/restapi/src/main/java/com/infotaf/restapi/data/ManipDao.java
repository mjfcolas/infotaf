package com.infotaf.restapi.data;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.Manip;

@Repository
public class ManipDao extends GenericDao<Manip> implements IManipDao{
	
	private static final Logger logger = LoggerFactory.getLogger(ManipDao.class);
	
	public Manip getManip(String name){
		logger.debug("IN - name: {}", name);
		Criterion whereName = Restrictions.eq("nom", name);
		
		Manip manip = (Manip) getSession()
					.createCriteria(Manip.class)
					.add(whereName)
					.uniqueResult();
		return manip;
	}
}