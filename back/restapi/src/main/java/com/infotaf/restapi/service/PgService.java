package com.infotaf.restapi.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infotaf.common.exceptions.PgFormatException;
import com.infotaf.common.utils.Utils;
import com.infotaf.restapi.data.PgDao;
import com.infotaf.restapi.data.PgManipDao;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.PgManip;
import com.infotaf.restapi.web.viewModel.PgComplete;

@Service
public class PgService implements IPgService{
	
	private static final Logger logger = LoggerFactory.getLogger(PgService.class);
	
	@Autowired
	protected PgDao pgDao;
	@Autowired
	protected PgManipDao pgManipDao;
	
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
	public void updatePassword(Pg pg){
		logger.debug("IN - nums: {}, tbk: {}, proms: {}", pg.getNums(), pg.getTbk() , pg.getProms());
		
		Pg pgDb = pgDao.getPg(pg.getNums(), pg.getTbk(), pg.getProms());
		if(pgDb != null){
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(pg.getPassword());
			pgDb.setPassword(encodedPassword);
			pgDao.update(pgDb);
		}
	}
}