package com.example.user_service.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;

    public LoginResponse(String token){
        this.token = token;
    }
}