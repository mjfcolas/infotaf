package com.infotaf.restapi.data;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.PgManip;

@Repository
public class PgDao extends GenericDao<Pg> implements IPgDao{
	
	public Pg getPg(String nums, String tbk, String proms){
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