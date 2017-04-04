package com.infotaf.restapi.service;

import java.util.List;

import com.infotaf.restapi.model.Manip;

public interface IManipService{
	/**
	 * Récupération d'une Manip par son Id
	 * @param id : id de la manip à récupérer
	 * @return un objet manip
	 */
	public Manip getManipById(int id);
	/**
	 * Sauvegarde d'une manip
	 * @param manip : Objet manip représentatif de la base
	 */
	public void saveManip(Manip manip);
	/**
	 * Sauvegarde de plusieurs manips
	 * @param manips : Les objets manip représentatif de la base à sauvegarder
	 */
	public void saveManips(List<Manip> manips);
	/**
	 * Récupération d'une manip à partir de son nom
	 * @param name : Nom de la manip à récupérer
	 * @return Un objet manip
	 */
	public Manip getManipByName(String name);
	/**
	 * Purge de la table manip
	 */
	public void deleteManips();
}