package com.yawka.xozcargo.payload;

import com.yawka.xozcargo.entity.Client;

import java.util.Objects;

public class UserRequestDTO {

    private String username;

    private Client client;

    private String password;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "UserRequestDTO{" +
                "username='" + username + '\'' +
                ", client=" + client +
                ", password='" + password + '\'' +
                '}';
    }
}
