package com.infotaf.restapi.security.model;

public class UserRole {
   
    protected Long id = new Long(0);
    protected Role role = Role.MEMBER;

    public Role getRole() {
        return role;
    }
}
