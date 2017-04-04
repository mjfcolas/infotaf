package com.infotaf.restapi.data;

import com.infotaf.restapi.model.Manip;

public interface IManipDao extends IGenericDao<Manip> {
	/**
	 * Récupération des manips selon la clé unique nom de la manip
	 * @param name
	 * @return
	 */
	public Manip getManip(String name);
}