package com.example.pangpang.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 회원가입시 아이디 중복 체크하는 DTO
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCheckIdInSignupDTO {

    @NotNull(message = "아이디는 필수 항목입니다.")
    private String memberId;
}
