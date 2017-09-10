package com.infotaf.restapi.data;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.Pg;

@Repository
@SuppressWarnings("unchecked")
public class PgDao extends GenericDao<Pg> implements IPgDao{
	
	private static final Logger logger = LoggerFactory.getLogger(PgDao.class);
	
	public Pg getPg(String nums, String tbk, String proms){
		logger.debug("IN - nums: {}, tbk: {}, proms: {}", nums, tbk , proms);
		Criterion whereNums = Restrictions.eq("nums", nums);
		Criterion whereTbk = Restrictions.eq("tbk", tbk).ignoreCase();
		Criterion whereProms = Restrictions.eq("proms", proms);
		
		Pg pg = (Pg) getSession()
				.createCriteria(Pg.class)
				.add(whereNums)
				.add(whereTbk)
				.add(whereProms)
				.uniqueResult();
		return pg;
	}
	
	public List<Pg> getPgsByRole(String role){
		logger.debug("IN - role: {}", role);
		Criterion whereRole = Restrictions.eq("role.role", role);
		
		List<Pg> pg = getSession()
				.createCriteria(Pg.class)
				.createAlias("roles", "role")
				.add(whereRole)
				.list();
		return pg;
	}
	
}