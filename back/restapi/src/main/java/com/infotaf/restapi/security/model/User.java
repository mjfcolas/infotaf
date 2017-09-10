package com.infotaf.restapi.security.model;

import java.util.List;

import com.infotaf.restapi.model.Role;

public class User {

    private String username;
    
    private String password;
    
    private List<Role> roles;
    
    public User() { }
    
    public User(String username, String password, List<Role> roles) {
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

    public List<Role> getRoles() {
        return roles;
    }
}
