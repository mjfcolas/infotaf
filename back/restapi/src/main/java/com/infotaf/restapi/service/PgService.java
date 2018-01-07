package com.infotaf.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infotaf.common.exceptions.BusinessException;
import com.infotaf.common.exceptions.PgFormatException;
import com.infotaf.common.utils.Utils;
import com.infotaf.restapi.config.AppConfig;
import com.infotaf.restapi.data.PgDao;
import com.infotaf.restapi.data.PgManipDao;
import com.infotaf.restapi.data.RoleDao;
import com.infotaf.restapi.model.BusinessStatus;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.PgManip;
import com.infotaf.restapi.model.Role;
import com.infotaf.restapi.security.model.RoleEnum;
import com.infotaf.restapi.web.viewModel.PgBase;
import com.infotaf.restapi.web.viewModel.PgComplete;

@Service
public class PgService implements IPgService{
	
	private static final Logger logger = LoggerFactory.getLogger(PgService.class);
	
	@Autowired
	protected PgDao pgDao;
	@Autowired
	protected PgManipDao pgManipDao;
	@Autowired
	protected RoleDao roleDao;
	
	@Transactional(readOnly = true)
	public Pg getPgById(int id){
		return pgDao.get(id);
	}
	
	@Transactional(readOnly = true)
	public Pg getPgByNumsTbkProms(String nums, String tbk, String proms){
		logger.debug("IN - nums: {}, tbk: {}, proms: {}", nums, tbk, proms);
		return pgDao.getPg(nums, tbk, proms);
	}
	
	@Transactional(readOnly = true)
	public List<PgBase> getPgsByRole(String role){
		logger.debug("IN - role: {}", role);
		List<Pg> rawPgs = pgDao.getPgsByRole(role);
		List<PgBase> result = new ArrayList<PgBase>();
		for (Pg rawPg : rawPgs) {
			result.add(new PgBase(rawPg));
		}
		return result;
	}
	
	@Transactional(readOnly = false)
	public BusinessStatus deleteRoleFromPg(int pgId, String senderNums, RoleEnum role) throws BusinessException, PgFormatException{
		logger.debug("IN - pgId: {}, sender: {}, role: {}",pgId, senderNums, role.name());

		BusinessStatus result = new BusinessStatus();
		
		Map<String, String> parsedPg;
		parsedPg = Utils.ParsePg(senderNums);
		
		if(RoleEnum.ADM == role){
			Pg sender = pgDao.getPg(parsedPg.get("nums"), parsedPg.get("tbk"), parsedPg.get("proms"));
			//Un admin ne peut pas se supprimer soi même
			if(sender.getId() == pgId){
				throw new BusinessException(AppConfig.messages.getProperty("notif.exception.cannotDeleteYourself"));
			}
			//On ne peut pas supprimer le dernier admin
			if(roleDao.countRole(role) == 1){
				throw new BusinessException(AppConfig.messages.getProperty("notif.exception.cannotDeleteLastAdmin"));
			}
		}
		
		roleDao.deleteRoleFromPg(pgId, role);
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public PgComplete getPgWithManips(String pgId){
		logger.debug("IN - pgId: {}", pgId);
		//Formattage de la clé unique sur laquelle le filtre va être effectué à partir du paramètre pgId
		Map<String, String> parsedPg;
		try {
			parsedPg = Utils.ParsePg(pgId);
		} catch (PgFormatException e) {
			return null;
		}
		
		//Récupération d'un Pg a partir de la clé unique fournie
		Pg pg = pgDao.getPg(parsedPg.get("nums"), parsedPg.get("tbk"), parsedPg.get("proms"));
		if(pg != null){
			List<PgManip> manips = pgManipDao.getManipsForPg(pg.getId());
			return new PgComplete(pg, manips);
		}
		else{
			return null;
		}
	}
	
	@Transactional(readOnly = true)
	public List<PgComplete> getAllPgsWithManips(){
		logger.debug("IN");
		
		//Récupération d'un Pg a partir de la clé unique fournie
		List<Pg> pgList = pgDao.findAll();
		List<PgComplete> result = new ArrayList<PgComplete>();
		for (Pg pg : pgList) {
			List<PgManip> manips = pgManipDao.getManipsForPg(pg.getId());
			result.add(new PgComplete(pg, manips));
		}
		
		return result;
	}
	
	@Transactional(readOnly = true)
	public Pg getPg(String pgId){
		logger.debug("IN - pgId: {}", pgId);
		//Formattage de la clé unique sur laquelle le filtre va être effectué à partir du paramètre pgId
		Map<String, String> parsedPg;
		try {
			parsedPg = Utils.ParsePg(pgId);
		} catch (PgFormatException e) {
			return null;
		}
		
		//Récupération d'un Pg a partir de la clé unique fournie
		Pg pg = pgDao.getPg(parsedPg.get("nums"), parsedPg.get("tbk"), parsedPg.get("proms"));
		return pg;
	}
	
	@Transactional(readOnly = false)
	public void savePg(Pg pg){
		pgDao.save(pg);
	}
	@Transactional(readOnly = false)
	public void savePgs(List<Pg> pgs) {
		for (Pg pg : pgs) {
			Pg pgdb = pgDao.getPg(pg.getNums(), pg.getTbk(), pg.getProms());
			if(pgdb == null){
				pgDao.save(pg);
			}
		}
	}
	@Transactional(readOnly = false)
	public void deletePg(){
    	pgDao.deleteAll();
	}
	
	@Transactional(readOnly = false)
	public void updateKifekoi(Pg pg){
		logger.debug("IN - nums: {}, tbk: {}, proms: {}", pg.getNums(), pg.getTbk() , pg.getProms());
		
		Pg pgDb = pgDao.getPg(pg.getNums(), pg.getTbk(), pg.getProms());
		if(pgDb != null){
			pgDb.setWork(pg.getWork());
			pgDb.setWorkplace(pg.getWorkplace());
			pgDb.setAddress(pg.getAddress());
			pgDb.setWorkDetails(pg.getWorkDetails());
			pgDao.update(pgDb);
		}
	}
	
	@Transactional(readOnly = false)
	public void updateAccount(Pg pg){
		logger.debug("IN - nums: {}, tbk: {}, proms: {}", pg.getNums(), pg.getTbk() , pg.getProms());
		
		Pg pgDb = pgDao.getPg(pg.getNums(), pg.getTbk(), pg.getProms());
		if(pgDb != null){
			pgDb.setMail(pg.getMail());
			if(pg.getPassword() != null && !"".equals(pg.getPassword().trim()) ){
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				String encodedPassword = encoder.encode(pg.getPassword());
				pgDb.setPassword(encodedPassword);
			}
			pgDao.update(pgDb);
		}
	}
	
	@Transactional(readOnly = false)
	public PgBase addRoleToPg(String pg, RoleEnum role) throws BusinessException, PgFormatException{
		logger.debug("IN - pg: {}", pg, role.name());
		
		Map<String, String> parsedPg;
		parsedPg = Utils.ParsePg(pg);
		
		Pg pgDb = pgDao.getPg(parsedPg.get("nums"), parsedPg.get("tbk"), parsedPg.get("proms"));
		
		if(pgDb == null){
			throw new BusinessException(AppConfig.messages.getProperty("notif.exception.noPg"));
		}
		
		List<RoleEnum> roles = pgDb.getRoles().stream().map(x -> x.getRole()).collect(Collectors.toList());
		
		if(roles.contains(role)){
			throw new BusinessException(AppConfig.messages.getProperty("notif.exception.roleAlreadyAdded"));
		}
		
		Role toAdd = new Role();
		toAdd.setPg(pgDb);
		toAdd.setRole(role);
		
		roleDao.save(toAdd);
		
		return new PgBase(pgDb);

	}
}