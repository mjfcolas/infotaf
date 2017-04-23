package com.infotaf.restapi.service;

import com.infotaf.restapi.model.Param;

public interface IParamService{
	/***
	 * Sauvegarde ou mise à jour d'un paramètre
	 * @param key
	 * @param value
	 * @return
	 */
	public int saveParam(String key, String value);
	/***
	 * Récupération d'un paramètre à l'aide de sa clé
	 * @param key
	 * @return
	 */
	public Param getParam(String key);
}