package com.yawka.xozcargo.payload;

import com.yawka.xozcargo.entity.Client;

import java.util.List;

public class JwtResponseDTO {

    private String token;

    private String type = "Bearer";
    private Long id;
    private String username;
    private Client client;
    private List<String> roles;

    public JwtResponseDTO(String token, Long id, String username, Client client, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.client = client;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
