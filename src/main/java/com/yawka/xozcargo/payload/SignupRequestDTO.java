package com.yawka.xozcargo.payload;

import java.util.Set;

public class SignupRequestDTO {

    private String username;

    private Set<String> role;

    private String password;

    ///GETTERS AND SETTERS///

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
