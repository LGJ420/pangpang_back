package com.example.pangpang.entity;

import java.util.*;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Member {

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
        private int memberBirth;
        
        // 유저 역할 (admin(관리자), user(일반유저))
        // 사람들이 회원가입할 때 <input type="hidden" name="role" value="user"> 로 숨겨두기
        private String memberRole;

        // ▲▲▲ 회원가입, ID/PW찾기 때 필요한 데이터 ▲▲▲


        @OneToMany(mappedBy = "member")
        private List<Orders> orders;

        @OneToMany(mappedBy = "member")
        private List<Article> articles;

        @OneToMany(mappedBy = "member")
        private List<Comment> comment;
}
