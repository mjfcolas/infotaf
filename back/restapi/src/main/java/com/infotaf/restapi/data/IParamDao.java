package com.infotaf.restapi.data;

import com.infotaf.restapi.model.Param;

public interface IParamDao extends IGenericDao<Param> {
	/***
	 * Récupration d'un paramètre à l'aide de sa clé unique
	 * @param key
	 * @return
	 */
	public Param getParam(String key);
	/***
	 * Sauvegarde ou mise à jour d'un paramètre
	 * @param key
	 * @param value
	 * @return
	 */
	public int saveParam(String key, String value);
}