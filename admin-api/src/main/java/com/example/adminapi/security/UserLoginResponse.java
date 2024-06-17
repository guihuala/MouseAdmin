package com.example.adminapi.security;

import com.example.adminapi.entity.User;

public class UserLoginResponse {
    private User user;
    private String token;

    public UserLoginResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}