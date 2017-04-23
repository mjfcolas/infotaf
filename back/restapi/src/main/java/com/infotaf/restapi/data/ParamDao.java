package com.infotaf.restapi.data;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.Param;

@Repository
public class ParamDao extends GenericDao<Param> implements IParamDao{
	
	private static final Logger logger = LoggerFactory.getLogger(ParamDao.class);
	
	public Param getParam(String key){
		logger.debug("IN - key: {}", key);
		Criterion whereKey = Restrictions.eq("key", key);
		
		Param param = (Param) getSession()
				.createCriteria(Param.class)
				.add(whereKey)
				.uniqueResult();
		return param;
	}
	
	public int saveParam(String key, String value){
		logger.debug("IN - param key: {}", key);
		
		Param toUpdate = this.getParam(key);
		if(toUpdate == null)
		{
			toUpdate = new Param();
			toUpdate.setKey(key);
		}
		toUpdate.setValue(value);
		return (int) this.save(toUpdate);
	}
}