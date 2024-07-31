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
public class MemberInFindPwForResetDTO {

    @NotNull(message = "아이디는 필수 항목입니다.")
    private String memberIdInFindPwForReset;

    @NotNull(message = "비밀번호 확인은 필수 항목입니다.")
    private String memberPwInFindPwForReset;
}
