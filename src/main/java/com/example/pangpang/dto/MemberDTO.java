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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO implements UserDetails {

    @NotNull
    private String memberId;
    
    @NotNull
    private String memberPw;
    
    @NotNull
    private String memberName;
    
    @NotNull
    @Size(min = 6, max = 6)
    private int memberBirth;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 설정 로직 (필요 시 구현)
        return null;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public String getPassword() {
        return memberPw;
    }


}
