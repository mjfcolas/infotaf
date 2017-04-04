package com.infotaf.restapi.service;

import java.util.List;

import com.infotaf.restapi.model.Manip;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.PgManip;

public interface IGlobalService{
	/**
	 * Sauvegarde de tous les objets parsés à partir du excel de taf 
	 * @param pgManips Objets pour la table pg_manip
	 * @param manips Objets pour la table manip
	 * @param pgs Objet pour la table pg
	 */
	public void saveExcel(List<PgManip> pgManips, List<Manip> manips, List<Pg> pgs);
}