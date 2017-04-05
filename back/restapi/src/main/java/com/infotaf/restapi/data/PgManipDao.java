package com.infotaf.restapi.data;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.PgManip;

@Repository
@SuppressWarnings("unchecked")
public class PgManipDao extends GenericDao<PgManip> implements IPgManipDao{
	
	private static final Logger logger = LoggerFactory.getLogger(PgManipDao.class);
	
	public List<PgManip> getManipsForPg(int pgId){
		logger.debug("{}", pgId);
		Criterion wherePgId = Restrictions.eq("pg.id", pgId);
		List<PgManip> manips = getSession()
				.createCriteria(PgManip.class)
				.setFetchMode("manip", FetchMode.JOIN)
				.add(wherePgId).list();
		return manips;
	}
}