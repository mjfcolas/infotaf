package com.infotaf.restapi.data;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.Role;
import com.infotaf.restapi.security.model.RoleEnum;

@Repository
@SuppressWarnings("unchecked")
public class RoleDao extends GenericDao<Role> implements IRoleDao{
	
	private static final Logger logger = LoggerFactory.getLogger(RoleDao.class);
	
	public List<Role> getRole(Pg pg){
		logger.debug("IN - pgid: {}", pg.getId());
		Criterion whereId = Restrictions.eq("pg.id", pg.getId());
		
		List<Role> result = getSession()
							.createCriteria(Role.class)
							.add(whereId)
							.list();
		return result;
	}
	
	public void deleteRoleFromPg(int pgId, RoleEnum role){
		logger.debug("IN - pgId: {}, role: {}", pgId, role.name());
		
		String hql = "delete from Role where pg.id= :pgId";
		getSession().createQuery(hql).setInteger("pgId", pgId).executeUpdate();
	}
	
	public long countRole(RoleEnum role){
		logger.debug("IN - rôle: {}", role.name());
		
		Criterion whereRole = Restrictions.eq("role", role.name());
		
		return (long)getSession()
				.createCriteria(Role.class)
				.add(whereRole)
				.setProjection(Projections.rowCount()).uniqueResult();
	}
	
}