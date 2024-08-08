package com.example.pangpang.dto;

// 로그인 토큰 DTO

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
