package com.example.pangpang.controller;

import java.util.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.MemberCheckIdInSignupDTO;
import com.example.pangpang.dto.MemberDTO;
import com.example.pangpang.dto.MemberInFindIdDTO;
import com.example.pangpang.dto.MemberInFindPwDTO;
import com.example.pangpang.dto.MemberInFindPwForResetDTO;
import com.example.pangpang.dto.MemberInLoginDTO;
import com.example.pangpang.dto.MemberInLoginResponseDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입 - 아이디 중복 확인
    @PostMapping("/signup/checkMemberId")
    public Map<String, String> chechMemberId(@Valid @RequestBody MemberCheckIdInSignupDTO memberCheckIdInSignupDTO) {
        
        memberService.checkMemberId(memberCheckIdInSignupDTO);

        return Map.of("result", "아이디 중복 확인 성공");
    }

    // 회원가입
    @PostMapping("/signup")
    public Map<String, String> signup(@Valid @RequestBody MemberDTO memberDTO) {

        memberService.createMember(memberDTO);

        return Map.of("result", "회원가입 성공!", "memberName", memberDTO.getMemberName());
    }

    // 아이디 찾기
    @PostMapping("/find_id")
    public Member findId(@Valid @RequestBody MemberInFindIdDTO memberInFindIdDTO) {

        Member memberInfo = memberService.findId(memberInFindIdDTO);

        return memberInfo;
    }

    // 비밀번호 찾기
    @PostMapping("/find_pw")
    public Member findPw(@RequestBody MemberInFindPwDTO memberInFindPwDTO) {

        Member memberInfo = memberService.findPw(memberInFindPwDTO);

        return memberInfo;
    }

    // 비밀번호 찾기->비밀번호 변경
    @PostMapping("/find_pw/reset")
    public Map<String, String> resetPw(@Valid @RequestBody MemberInFindPwForResetDTO memberInFindPwResetForDTO) {

        memberService.resetPw(memberInFindPwResetForDTO);

        return Map.of("result", "비밀번호 변경 성공!");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberInLoginDTO memberInLoginDTO) {
        String token = memberService.login(memberInLoginDTO);

        // 200 OK 상태 코드와 함께 JWT 포함 응답 반환
        return ResponseEntity.ok().body(new MemberInLoginResponseDTO(token));
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout() {

        // 로그아웃 성공 메세지
        return "로그아웃 성공";
    }

}
