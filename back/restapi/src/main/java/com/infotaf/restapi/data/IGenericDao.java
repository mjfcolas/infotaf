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
	 * Mise à jour d'un objet E en se basant sur sa clé primaire
	 * @param entity objet considéré 
	 */
	public void update(E entity);
	/**
	 * Purge d'une table
	 */
	public void deleteAll();
	/**
	 * Suppression d'un élément d'une table
	 * @param entity Element à supprimer
	 */
	public void delete(E entity);
	/**
	 * Récupération d'une table
	 * @return
	 */
	public List<E> findAll();
}