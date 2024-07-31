package com.example.pangpang.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInFindPwDTO {

    @NotNull(message = "아이디는 필수 항목입니다.")
    private String memberIdInFindPw;

    @NotNull(message = "비밀번호 확인은 필수 항목입니다.")
    private String memberNameInFindPw;

    @NotNull(message = "생년월일은 필수 항목입니다.")
    @Size(min = 6, max = 6, message = "생년월일은 숫자 6자리로 입력해주세요.")
    private String memberBirthInFindPw;

}
