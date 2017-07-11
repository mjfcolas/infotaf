package com.infotaf.restapi.security.model;

import java.util.List;

public class User {

    private String username;
    
    private String password;
    
    private List<UserRole> roles;
    
    public User() { }
    
    public User(String username, String password, List<UserRole> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<UserRole> getRoles() {
        return roles;
    }
}
