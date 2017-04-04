package com.infotaf.restapi.service;

import java.util.List;

import com.infotaf.restapi.model.PgManip;

public interface IPgManipService{
	/**
	 * Sauvegarde sans update d'un lien entre pg et manip
	 * @param pgManips l'objet pgManip à sauvegarder
	 */
	public void savePgManips(List<PgManip> pgManips);
	/**
	 * Purge de la table pg_manip en base
	 */
	public void deletePgManips();
}