package com.infotaf.restapi.data;

import java.util.List;

import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.Role;
import com.infotaf.restapi.security.model.RoleEnum;

public interface IRoleDao extends IGenericDao<Role> {
	/**
	 * Récupération des roles d'un Pg
	 * @param pg : pg dont on veut les roles
	 * @return Liste des roles du pg
	 */
	public List<Role> getRole(Pg pg);
	/**
	 * Suppression d'un rôle d'un PG
	 * @param pgId : Id du pg dont on veut supprimer le rôle
	 * @param role : rôle à supprimer
	 */
	public void deleteRoleFromPg(int pgId, RoleEnum role);
	/**
	 * Compte le nombre de PG possédant un rôle
	 * @param role : rôle dont il faut compter les PG
	 * @return
	 */
	public long countRole(RoleEnum role);
}