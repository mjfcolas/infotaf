package com.infotaf.common.exceptions;

import org.springframework.security.core.AuthenticationException;

import com.infotaf.restapi.security.model.token.JwtToken;

/**
 * 
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
public class JwtExpiredTokenException extends AuthenticationException {
    private static final long serialVersionUID = -5959543783324224864L;
    
    private JwtToken token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
        System.out.println(msg);
    }

    public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
        System.out.println(msg);
    }

    public String token() {
        return this.token.getToken();
    }
}
