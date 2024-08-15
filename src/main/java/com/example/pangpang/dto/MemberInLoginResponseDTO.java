package com.example.pangpang.dto;

// 로그인하면 토큰을 발급할 때 사용하는 DTO 

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
