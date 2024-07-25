package com.example.pangpang.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class member {

    // ▼▼▼ 로그인할 때 필요한 데이터 ▼▼▼

        // 고유식별번호
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // 유저 아이디
        @Column(unique = true)
        @NotNull
        private String member_id;
        
        // 유저 비밀번호
        @Column(nullable = false)
        @NotNull
        private String password;
        
                // ▲▲▲ 로그인할 때 필요한 데이터 ▲▲▲
        
        // =============================================
        
        // ▼▼▼ 회원가입, ID/PW찾기 때 필요한 데이터 ▼▼▼
        
        // 유저 이름
        @NotNull
        private String member_name;
        
        // 유저 생년월일(주민번호 6자리)
        @NotNull
        @Size(min = 6, max = 6)
        private String member_number;
        
        // 유저 역할 (admin(관리자), user(일반유저))
        // 사람들이 회원가입할 때 <input type="hidden" name="role" value="user"> 로 숨겨두기
        private String role;

    // ▲▲▲ 회원가입, ID/PW찾기 때 필요한 데이터 ▲▲▲
}
