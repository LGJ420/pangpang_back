package com.example.pangpang.entity;

import java.util.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Member implements UserDetails {

        // 로그인할 때 필요한 데이터

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

        // 활동(true), 활동정지(isActive) 구분
        private boolean isActive = true;

        // =============================================

        // 회원가입, ID/PW찾기 때 필요한 데이터

        // 유저 이름
        private String memberName;

        // 유저 닉네임
        private String memberNickname;

        // 유저 생년월일(주민번호 6자리)
        private String memberBirth;

        // 유저 가입 날짜
        private LocalDateTime memberSignupDate;

        // 유저 역할 (admin(관리자), user(일반유저))
        // 사람들이 회원가입할 때 <input type="hidden"> 로 숨겨두기
        private String memberRole;

        // ==============================================

        // 내정보 변경으로 바꿀 수 있는 내용

        // 프로필 사진
        private String memberImage;

        // 기본 배송지
        private String memberAddress;

        // 밑으로 UserDetails 인터페이스 상속 메서드
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                if (memberRole == null || memberRole.isEmpty()) {
                        return Collections.emptyList();
                }
                return Arrays.stream(memberRole.split(","))
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim().toUpperCase()))
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

                throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
        }
}
