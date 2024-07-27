package com.example.pangpang.entity;

import java.util.*;
import jakarta.persistence.*;

import lombok.*;

/*
 * 이 주석은 지워도 읽고 지워도됩니다
 * 
 * 1. 클래스 이름은 대문자라서 member -> Meber 수정했어요
 * 2. 자바는 카멜기법 써야되서 member_id -> memberId 수정했어요
 * 3. 생년월일은 Birth가 좋을거같아서 바꿨어요
 * 4. 검증은 DTO단계에서 하기로 해서 검증애노테이션들은 DTO로 옮겼어요 (@NotNull같은것들)
 */
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
        private String role;

        // ▲▲▲ 회원가입, ID/PW찾기 때 필요한 데이터 ▲▲▲


        @OneToMany
        private List<Orders> orders;

        @OneToMany
        private List<Article> articles;

        @OneToMany
        private List<Comment> comment;
}
