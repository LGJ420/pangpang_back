package com.example.pangpang.dto;

public class MemberInLoginResponseDTO {

    private String token;

    public MemberInLoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
