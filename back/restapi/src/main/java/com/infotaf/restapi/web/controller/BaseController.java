package com.infotaf.restapi.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infotaf.restapi.model.Manip;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.service.ManipService;
import com.infotaf.restapi.service.PgService;
import com.infotaf.restapi.web.viewModel.PgComplete;

@RestController
@CrossOrigin
public class BaseController {

	@Autowired
	protected PgService pgService;
	@Autowired
	protected ManipService manipService;
	
	/**
	 * Récupération d'un pg avec toutes les informations liées aux manips auxquelles il a participé
	 * @param pg
	 * @return
	 */
	@RequestMapping("Pg")
	public PgComplete GetPg(String pg){
		PgComplete toReturn = pgService.getPgWithManips(pg);
		return toReturn;
	}
}
