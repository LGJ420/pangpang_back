package com.example.pangpang.entity;

import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Member implements UserDetails {

        // ▼▼▼ 로그인할 때 필요한 데이터 ▼▼▼

        // 고유식별번호
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // 유저 아이디
        @Column(unique = true)
        private String memberId;

        // 유저 비밀번호
        @Column(nullable = false)
        private String memberPw;

        // ▲▲▲ 로그인할 때 필요한 데이터 ▲▲▲

        // =============================================

        // ▼▼▼ 회원가입, ID/PW찾기 때 필요한 데이터 ▼▼▼

        // 유저 이름
        private String memberName;

        // 유저 생년월일(주민번호 6자리)
        private String memberBirth;

        // 유저 역할 (admin(관리자), user(일반유저))
        // 사람들이 회원가입할 때 <input type="hidden"> 로 숨겨두기
        private String memberRole;

        // ▲▲▲ 회원가입, ID/PW찾기 때 필요한 데이터 ▲▲▲

        // 추가 필드
        private String roles; // 역할을 저장하는 필드

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                if (roles == null || roles.isEmpty()) {
                        return Collections.emptyList();
                }
                return Arrays.stream(roles.split(","))
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
        }

        @Override
        public String getPassword() {
                return memberPw; // memberPw를 password로 사용
        }

        @Override
        public String getUsername() {
                return memberId; // memberId를 username으로 사용
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return true;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }

        public Member orElseThrow(Object object) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
        }
}
