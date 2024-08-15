package com.example.pangpang.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/*
 * DTO는 정말 웹이랑 주고받는 객체
 * 그래서 정말 필요한것만 주고받는다
 * DTO단계에서 검증을 한다 (널값인지 사이즈는 맞는지)
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    // 비밀번호변경
    private Long id;

    // 아이디 중복(회원가입), 회원가입, 아이디찾기, 비밀번호찾기, 내정보수정(마이페이지)
    @NotNull(message = "아이디는 필수 항목입니다.")
    private String memberId;

    // 회원가입, 로그인, 비밀번호확인(마이페이지), 내정보수정(마이페이지)
    @NotNull(message = "비밀번호는 필수 항목입니다.")
    private String memberPw;

    // 회원가입, 아이디찾기, 비밀번호찾기
    @NotNull(message = "이름은 필수 항목입니다.")
    private String memberName;

    // 회원가입, 내정보수정(마이페이지)
    @NotNull(message = "닉네임은 필수 항목입니다")
    private String memberNickname;

    // 회원가입, 아이디찾기, 비밀번호찾기
    @NotNull(message = "생년월일은 필수 항목입니다.")
    @Size(min = 6, max = 6, message = "생년월일은 숫자 6자리로 입력해주세요.")
    private String memberBirth;

    // 회원가입, 내정보수정(마이페이지)
    @NotNull(message = "핸드폰 번호는 필수 항목입니다")
    private String memberPhone;

    // 회원가입, 내정보수정(마이페이지)
    @NotNull(message = "주소는 필수 항목입니다")
    private String postcode; // 우편번호
    private String postAddress; // 경기도 성남시 어쩌고저쩌고
    private String detailAddress; // 101동 505호
    private String extraAddress; // (미금동?)

    // 회원가입
    private String memberRole;
}
