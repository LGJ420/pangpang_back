package com.example.pangpang.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInLoginDTO {

    @NotNull(message = "아이디는 필수 항목입니다.")
    private String memberIdInLogin;

    @NotNull(message = "비밀번호는 필수 항목입니다.")
    private String memberPwInLogin;
}
