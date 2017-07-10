package com.infotaf.restapi.service;

import java.util.List;

import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.web.viewModel.PgComplete;

public interface IPgService{
	/**
	 * Récupération d'un objet pg via son Id
	 * @param id : id technique du PG à récupérer
	 * @return un objet Pg représentatif de la base
	 */
	public Pg getPgById(int id);
	/**
	 * Récupération d'un Pg avec la liste des manips qui lui sont associés
	 * @param pgId : concatnéation nums tbk proms
	 * @return Un pg avec la liste des manips qui lui sont associées
	 */
	public PgComplete getPgWithManips(String pgId);
	/**
	 * Sauvegarde sans update d'un Pg 
	 * @param pg : l'objet Pg représentatif de la base à sauvegarder
	 */
	public void savePg(Pg pg);
	/**
	 * Sauvegarde sans updates d'une liste de Pg
	 * @param pgs Les objets Pg à sauvegarder
	 */
	public void savePgs(List<Pg> pgs);
	/**
	 * Récupétion d'un Pg représentatif de la base à partir de la clé unique nums tbk proms
	 * @param nums : Nums du Pg
	 * @param tbk : Tabagns du Pg
	 * @param proms : Proms du Pg
	 * @return Un objet Pg représentatif de la base
	 */
	public Pg getPgByNumsTbkProms(String nums, String tbk, String proms);
	/**
	 * Purge de la table pg en base
	 */
	public void deletePg();
	/**
	 * Mise à jour des informations kifekoi en se basant sur la clé unique nums tbk proms
	 * @param pg le Pg avec les informations kifekoi à jour
	 */
	public void updateKifekoi(Pg pg);
	/**
	 * Modification du mot de passe d'un PG
	 * @param pg le Pg avec le nouveau mot de passe non encodé
	 */
	public void updatePassword(Pg pg);
}