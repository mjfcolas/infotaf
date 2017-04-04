package com.infotaf.restapi.data;

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
}