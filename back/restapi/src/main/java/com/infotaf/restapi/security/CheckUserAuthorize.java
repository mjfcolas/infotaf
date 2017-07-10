package com.infotaf.restapi.security;

import com.infotaf.common.exceptions.UnauthorizedUserException;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.security.auth.JwtAuthenticationToken;
import com.infotaf.restapi.security.model.UserContext;

public class CheckUserAuthorize {
	
	/**
	 * Vérification d'un Pg vis à vis de l'utilisateur connecté
	 * @param principal Jeton de l'utilisateur connecté
	 * @param pg L'objet PG à vérifier
	 * @throws UnauthorizedUserException
	 */
	public static void checkUser(JwtAuthenticationToken principal, Pg pg) throws UnauthorizedUserException{
		String loggedUser = ((UserContext)principal.getPrincipal()).getUsername();
		
		String nums = pg.getNums();
		String tbk = pg.getTbk();
		String proms = pg.getProms();
		
		if(loggedUser == null 
				|| nums  == null || nums.isEmpty()
				|| tbk   == null || tbk.isEmpty()
				|| proms == null || proms.isEmpty()
				|| !loggedUser.toUpperCase().equals(nums.concat(tbk).concat(proms).toUpperCase())){
			throw new UnauthorizedUserException();
		}
	}

}
