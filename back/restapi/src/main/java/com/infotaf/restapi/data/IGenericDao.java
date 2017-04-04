package com.infotaf.restapi.data;

import java.io.Serializable;
import java.util.List;

public interface IGenericDao<E>{
	/**
	 * Récupération d'un objet E par sa clé primaire
	 * @param id : clé primaire
	 * @return : Instance de l'objet considéré
	 */
	E get(Serializable id );
	/**
	 * Sauvegarde sans update d'une objet E
	 * @param entity : objet considéré
	 * @return Identifiant de l'objet récupéré
	 */
	Serializable save(E entity);
	/**
	 * Purge d'une table
	 */
	public void deleteAll();
	/**
	 * Récupération d'une table
	 * @return
	 */
	public List<E> findAll();
}