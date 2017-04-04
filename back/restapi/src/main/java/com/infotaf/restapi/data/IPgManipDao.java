package com.infotaf.restapi.data;

import java.util.List;

import com.infotaf.restapi.model.PgManip;

public interface IPgManipDao extends IGenericDao<PgManip> {
	/**
	 * Récupération d'une liste de panip à partir de l'id technique d'un Pg
	 * @param pgId : Id technique du Pg demandé
	 * @return Liste d'objet représéntant la table pg_manip 
	 */
	public List<PgManip> getManipsForPg(int pgId);
}