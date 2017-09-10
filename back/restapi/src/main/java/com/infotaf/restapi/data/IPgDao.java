package com.infotaf.restapi.data;

import java.util.List;

import com.infotaf.restapi.model.Pg;

public interface IPgDao extends IGenericDao<Pg> {
	/**
	 * Récupération d'un Pg selon la clé unique nums-tbk-proms
	 * @param nums : Nums du pg
	 * @param tbk : tabagns du Pg
	 * @param proms : proms du Pg
	 * @return
	 */
	public Pg getPg(String nums, String tbk, String proms);
	/**
	 * Récupération de tous les PGs qui possèdent le rôle donné
	 * @param role : rôle des PG
	 * @return
	 */
	public List<Pg> getPgsByRole(String role);
}