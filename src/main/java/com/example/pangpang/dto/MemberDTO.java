package com.example.pangpang.dto;

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
public class MemberDTO {

    @NotNull(message = "아이디는 필수 항목입니다.")
    private String memberId;

    @NotNull(message = "비밀번호는 필수 항목입니다.")
    private String memberPw;

    @NotNull(message = "비밀번호 확인은 필수 항목입니다.")
    private String memberName;

    @NotNull(message = "닉네임은 필수 항목입니다")
    private String memberNickname;

    @NotNull(message = "생년월일은 필수 항목입니다.")
    @Size(min = 6, max = 6, message = "생년월일은 숫자 6자리로 입력해주세요.")
    private String memberBirth;

    @NotNull(message = "핸드폰 번호는 필수 항목입니다")
    private String memberPhone;

    @NotNull(message = "주소는 필수 항목입니다")
    private String postcode; // 우편번호
    private String postAddress; // 경기도 성남시 어쩌고저쩌고
    private String detailAddress; // 101동 505호
    private String extraAddress; // (미금동?)

    private String memberRole;
}
