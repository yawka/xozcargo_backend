package com.yawka.xozcargo.payload;

import com.yawka.xozcargo.entity.Client;

public class UserResponseDTO {
    private Long userId;

    private String username;

    private Client client;

    public UserResponseDTO(Long userId, String username, Client client) {
        this.userId = userId;
        this.username = username;
        this.client = client;
    }
}

