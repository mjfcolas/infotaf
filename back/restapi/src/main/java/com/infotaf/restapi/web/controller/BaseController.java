package com.infotaf.restapi.web.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infotaf.restapi.model.News;
import com.infotaf.restapi.service.ManipService;
import com.infotaf.restapi.service.NewsService;
import com.infotaf.restapi.service.ParamService;
import com.infotaf.restapi.service.PgService;
import com.infotaf.restapi.web.viewModel.Infos;
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
	 * Récupération d'un pg avec toutes les informations liées aux manips auxquelles il a participé
	 * @param pg
	 * @return
	 */
	@RequestMapping("Pg")
	public PgComplete GetPg(String pg){
		logger.debug("IN - pg: {}", pg);

		PgComplete toReturn = pgService.getPgWithManips(pg);
		
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
}
