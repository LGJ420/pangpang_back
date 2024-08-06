package com.example.pangpang.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.*;
import lombok.*;

/*
 * DTO는 정말 웹이랑 주고받는 객체
 * 그래서 정말 필요한것만 주고받는다
 * DTO단계에서 검증을 한다 (널값인지 사이즈는 맞는지)
 */

// 회원가입 DTO

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO{

    @NotNull(message = "아이디는 필수 항목입니다.")
    private String memberId;

    @NotNull(message = "비밀번호는 필수 항목입니다.")
    private String memberPw;

    @NotNull(message = "비밀번호 확인은 필수 항목입니다.")
    private String memberName;

    @NotNull(message = "생년월일은 필수 항목입니다.")
    @Size(min = 6, max = 6, message = "생년월일은 숫자 6자리로 입력해주세요.")
    private String memberBirth;

    private String memberRole;
}
