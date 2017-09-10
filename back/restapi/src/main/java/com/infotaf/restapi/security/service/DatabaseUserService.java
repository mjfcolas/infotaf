package com.infotaf.restapi.security.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infotaf.common.exceptions.PgFormatException;
import com.infotaf.common.utils.Utils;
import com.infotaf.restapi.data.PgDao;
import com.infotaf.restapi.data.RoleDao;
import com.infotaf.restapi.model.Pg;
import com.infotaf.restapi.model.Role;
import com.infotaf.restapi.security.model.RoleEnum;
import com.infotaf.restapi.security.model.User;

/**
 * Mock implementation.
 * 
 * @author vladimir.stankovic
 *
 * Aug 4, 2016
 */
@Service
public class DatabaseUserService{
    
	private static final Logger logger = LoggerFactory.getLogger(DatabaseUserService.class);
	
	@Autowired
    protected PgDao pgDao;
	@Autowired
    protected RoleDao roleDao;
	
    @Autowired
    public DatabaseUserService() {
    }
    
    @Transactional(readOnly = false)
    public Optional<User> getByUsername(String username) {
    	logger.debug("IN - username: {}", username);
    	
    	
		//Formattage de la clé unique sur laquelle le filtre va être effectué à partir du paramètre pgId
		Map<String, String> parsedPg;
		try {
			parsedPg = Utils.ParsePg(username);
		} catch (PgFormatException e) {
			return Optional.ofNullable(null);
		}
    	
		Pg pg = pgDao.getPg(parsedPg.get("nums"), parsedPg.get("tbk"), parsedPg.get("proms"));
		
		List<Role> genericRoles = roleDao.getRole(pg);
		List<RoleEnum> filteredRoles = genericRoles.stream()
				.map(x -> x.getRole())
				.filter(x -> x == RoleEnum.USR)
				.collect(Collectors.toList());
		if(filteredRoles.size() < 1){
			Role usrRole = new Role();
			usrRole.setPg(pg);
			usrRole.setRole(RoleEnum.USR);
			genericRoles.add(usrRole);
		}
    	
		//Initialisation du mot de passe au nom d'utilisateur si inexistant
		String password = pg.getPassword();
		if(password == null || password.isEmpty()){
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			//Mot de passe sensible à la casse
			String csUsername = pg.getNums().concat(pg.getTbk()).concat(pg.getProms());
			password = encoder.encode(csUsername);
			pg.setPassword(password);
			pgDao.update(pg);
		}
		
    	User user = new User(username, password, genericRoles);
        return Optional.ofNullable(user);
    }
}
