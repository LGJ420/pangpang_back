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

    @NotNull
    private String memberId;
    
    @NotNull
    private String memberPw;
    
    @NotNull
    private String memberName;
    
    @NotNull
    @Size(min = 6, max = 6)
    private int memberBirth;

}
