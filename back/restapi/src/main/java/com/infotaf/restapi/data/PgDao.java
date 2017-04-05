package com.infotaf.restapi.data;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.Pg;

@Repository
public class PgDao extends GenericDao<Pg> implements IPgDao{
	
	private static final Logger logger = LoggerFactory.getLogger(PgDao.class);
	
	public Pg getPg(String nums, String tbk, String proms){
		logger.debug("IN - nums: {}, tbk: {}, proms: {}", nums, tbk , proms);
		Criterion whereNums = Restrictions.eq("nums", nums);
		Criterion whereTbk = Restrictions.eq("tbk", tbk);
		Criterion whereProms = Restrictions.eq("proms", proms);
		
		Pg pg = (Pg) getSession()
				.createCriteria(Pg.class)
				.add(whereNums)
				.add(whereTbk)
				.add(whereProms)
				.uniqueResult();
		return pg;
	}
}