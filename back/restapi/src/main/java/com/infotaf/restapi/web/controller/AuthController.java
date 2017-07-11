package com.infotaf.restapi.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infotaf.restapi.security.auth.JwtAuthenticationToken;
import com.infotaf.restapi.security.model.UserContext;

@RestController
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@RequestMapping("auth/username")
	public UserContext CurrentUser(JwtAuthenticationToken principal){
		logger.debug("IN");
		return (UserContext) principal.getPrincipal();
	}

}
