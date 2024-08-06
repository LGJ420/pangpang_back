package com.example.pangpang.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 로그인 DTO

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInLoginDTO {

    @NotNull(message = "아이디는 필수 항목입니다.")
    @NotBlank
    private String memberIdInLogin;
    
    @NotNull(message = "비밀번호는 필수 항목입니다.")
    @NotBlank
    private String memberPwInLogin;
}
