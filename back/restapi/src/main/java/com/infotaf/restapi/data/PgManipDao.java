package com.infotaf.restapi.data;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.PgManip;

@Repository
@SuppressWarnings("unchecked")
public class PgManipDao extends GenericDao<PgManip> implements IPgManipDao{
	
	public List<PgManip> getManipsForPg(int pgId){
		Criterion wherePgId = Restrictions.eq("pg.id", pgId);
		List<PgManip> manips = getSession()
				.createCriteria(PgManip.class)
				.setFetchMode("manip", FetchMode.JOIN)
				.add(wherePgId).list();
		return manips;
	}
}