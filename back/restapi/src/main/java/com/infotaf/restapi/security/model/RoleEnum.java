package com.infotaf.restapi.security.model;

/**
 * Enumerated {@link User} roles.
 * 
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public enum RoleEnum {
    ADM, USR;
    
    public String authority() {
        return "ROLE_" + this.name();
    }
}
