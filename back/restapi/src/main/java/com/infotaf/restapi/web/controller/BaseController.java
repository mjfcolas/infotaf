package com.infotaf.restapi.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infotaf.common.exceptions.BusinessException;
import com.infotaf.common.exceptions.PgFormatException;
import com.infotaf.common.exceptions.UnauthorizedUserException;
import com.infotaf.restapi.config.AppConfig;
import com.infotaf.restapi.model.BusinessStatus;
import com.infotaf.restapi.model.News;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.security.CheckUserAuthorize;
import com.infotaf.restapi.security.auth.JwtAuthenticationToken;
import com.infotaf.restapi.security.model.RoleEnum;
import com.infotaf.restapi.security.model.UserContext;
import com.infotaf.restapi.service.ManipService;
import com.infotaf.restapi.service.NewsService;
import com.infotaf.restapi.service.ParamService;
import com.infotaf.restapi.service.PgService;
import com.infotaf.restapi.web.viewModel.Infos;
import com.infotaf.restapi.web.viewModel.NewsView;
import com.infotaf.restapi.web.viewModel.PgBase;
import com.infotaf.restapi.web.viewModel.PgComplete;

@RestController
@CrossOrigin
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	protected PgService pgService;
	@Autowired
	protected ManipService manipService;
	@Autowired
	protected ParamService paramService;
	@Autowired
	protected NewsService newsService;
	
	/**
	 * Retourne le timestamp du serveur
	 * @return
	 */
	@RequestMapping("Timestamp")
	public BusinessStatus GetTimestamp(){
		logger.debug("IN");
		Long timestamp = System.currentTimeMillis()/1000;
		
		BusinessStatus result = new BusinessStatus();
		result.setSuccess(true);
		result.setValue(timestamp.toString());
		return result;
	}
	
	/**
	 * Récupération d'un pg avec toutes les informations liées aux manips auxquelles il a participé
	 * @param pg
	 * @return
	 */
	@RequestMapping("auth/Pg")
	public PgComplete GetPg(String pg){
		logger.debug("IN - pg: {}", pg);

		PgComplete toReturn = pgService.getPgWithManips(pg);
		
		if(toReturn == null){
			toReturn = new PgComplete();
		}
		
		return toReturn;
	}
	
	/**
	 * Récupération des informations générales
	 * @return
	 */
	@RequestMapping("Infos")
	public Infos GetInfos(){
		logger.debug("IN");
		Infos toReturn = new Infos();
		try {
			Date updateDate= paramService.getDateFromParam();
			toReturn.setDate(updateDate);
		} catch (ParseException e) {
			e.printStackTrace();
			toReturn = new Infos();
		}
		return toReturn;
	}
	
	/**
	 * Récupération des news en base
	 * @return
	 */
	@RequestMapping("News")
	public List<News> GetNews(){
		logger.debug("IN");
		List<News> toReturn = newsService.getNews();
		return toReturn;
	}
	
	/**
	 * Création d'une news
	 * @param principal utilisateur connecté
	 * @param news news pasée
	 * @return Résultat de l'action
	 */
	@RequestMapping("auth/CreateNews")
	public BusinessStatus CreateNews(JwtAuthenticationToken principal, News news){
		logger.debug("IN");
		BusinessStatus result = new BusinessStatus();

		try{
			CheckUserAuthorize.checkUser(principal, RoleEnum.ADM);
			
			int id = newsService.saveNews(news);
			news.setId(id);
			NewsView returnNews = new NewsView(news);
			
			result.setObject(returnNews);
			result.setSuccess(true);
			result.setMessage(AppConfig.messages.getProperty("notif.success"));
		}catch(UnauthorizedUserException e){
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unauthorizedUser"));
			
		}catch(Exception e){
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unknownError"));
		}
		
		return result;
	}
	
	/**
	 * Création d'une news
	 * @param principal utilisateur connecté
	 * @param news news pasée
	 * @return Résultat de l'action
	 */
	@RequestMapping("auth/deleteNews")
	public BusinessStatus DeleteNews(JwtAuthenticationToken principal, int newsId){
		logger.debug("IN - {0}", newsId);
		BusinessStatus result = new BusinessStatus();

		try{
			CheckUserAuthorize.checkUser(principal, RoleEnum.ADM);
			newsService.deleteNews(newsId);
			
			result.setSuccess(true);
			result.setMessage(AppConfig.messages.getProperty("notif.success"));			
		}catch(UnauthorizedUserException e){
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unauthorizedUser"));
			
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unknownError"));
		}
		return result;
	}
	
	/**
	 * Récupération de la liste des admins
	 * @param principal utilisateur connecté
	 * @return liste des admins
	 */
	@RequestMapping("auth/getAdmins")
	public List<PgBase> GetAdmins(JwtAuthenticationToken principal){
		logger.debug("IN");
		try{
			CheckUserAuthorize.checkUser(principal, RoleEnum.ADM);
			return pgService.getPgsByRole(RoleEnum.ADM.name());
			
		}catch(UnauthorizedUserException e){
			return new ArrayList<PgBase>();
			
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<PgBase>();
		}
	}
	
	/**
	 * Récupération de la liste des admins
	 * @param principal utilisateur connecté
	 * @param pg PG à ajouter en tant qu'admin
	 * @return Résultat de l'opération
	 */
	@RequestMapping("auth/addAdmin")
	public BusinessStatus addAdmin(JwtAuthenticationToken principal, String pg){
		logger.debug("IN - pg: {}", pg);
		BusinessStatus result = new BusinessStatus();

		try{
			CheckUserAuthorize.checkUser(principal, RoleEnum.ADM);
			PgBase pgwWithRoleAdded = pgService.addRoleToPg(pg, RoleEnum.ADM);
			
			result.setObject(pgwWithRoleAdded);
			result.setSuccess(true);
			result.setMessage(AppConfig.messages.getProperty("notif.success"));			
		}catch(UnauthorizedUserException e){
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unauthorizedUser"));
		}catch(PgFormatException e){
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.badPgFormat"));
		}catch(BusinessException e){
			result.setSuccess(false);
			result.setMessage(e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unknownError"));
		}
		return result;
	}
	
	/**
	 * Récupération de la liste des admins
	 * @param principal utilisateur connecté
	 * @param pgId id de l'utilisateur à supprimer
	 * @return liste des admins
	 */
	@RequestMapping("auth/deleteAdmin")
	public BusinessStatus deleteAdmin(JwtAuthenticationToken principal, int pgId){
		logger.debug("IN");
		BusinessStatus result = new BusinessStatus();

		try{
			CheckUserAuthorize.checkUser(principal, RoleEnum.ADM);
			String sender = ((UserContext)principal.getPrincipal()).getUsername();
			pgService.deleteRoleFromPg(pgId, sender, RoleEnum.ADM);
			
			result.setSuccess(true);
			result.setMessage(AppConfig.messages.getProperty("notif.success"));			
		}catch(UnauthorizedUserException e){
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unauthorizedUser"));
			
		}catch(BusinessException e){
			result.setSuccess(false);
			result.setMessage(e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unknownError"));
		}
		return result;
	}
	
	/**
	 * Sauvegarde des infos du kifekoi
	 * @return
	 */
	@RequestMapping("auth/SaveKifekoi")
	public BusinessStatus SaveKifekoi(JwtAuthenticationToken principal, Pg pg){
		BusinessStatus result = new BusinessStatus();
		try{
			CheckUserAuthorize.checkUser(principal, pg);
			
			pgService.updateKifekoi(pg);
			
			result.setSuccess(true);
			result.setMessage(AppConfig.messages.getProperty("notif.success"));
		}catch(UnauthorizedUserException e){
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unauthorizedUser"));
			
		}
		catch(Exception e){
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unknownError"));
		}
		
		return result;
	}
	
	/**
	 * Changement de mot de passe
	 * @return
	 */
	@RequestMapping("auth/ChangePassword")
	public BusinessStatus ChangePassword(JwtAuthenticationToken principal, Pg pg){
		BusinessStatus result = new BusinessStatus();
		try{
			CheckUserAuthorize.checkUser(principal, pg);
			
			pgService.updatePassword(pg);
			
			result.setSuccess(true);
			result.setMessage(AppConfig.messages.getProperty("notif.success"));
		}catch(UnauthorizedUserException e){
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unauthorizedUser"));
			
		}catch(Exception e){
			result.setSuccess(false);
			result.setMessage(AppConfig.messages.getProperty("notif.exception.unknownError"));
		}
		
		return result;
	}

}
