package com.infotaf.restapi.data;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.Manip;
import com.infotaf.restapi.model.Pg;

@Repository
public class ManipDao extends GenericDao<Manip> implements IManipDao{
	public Manip getManip(String name){
		Criterion whereName = Restrictions.eq("nom", name);
		
		Manip manip = (Manip) getSession()
					.createCriteria(Manip.class)
					.add(whereName)
					.uniqueResult();
		return manip;
	}
}