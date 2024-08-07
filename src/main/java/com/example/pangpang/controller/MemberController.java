package com.example.pangpang.controller;

import java.util.*;
import java.security.Principal;

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
import com.example.pangpang.exception.MemberNotFoundException;
import com.example.pangpang.service.MemberService;
import com.example.pangpang.util.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private JwtUtil jwtUtil;

    // 로그인
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody MemberInLoginDTO memberInLoginDTO) {
        Member member = memberService.login(memberInLoginDTO);

        String memberId = member.getMemberId();
        String memberName = member.getMemberName();
        String memberRole = member.getMemberRole();

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(memberId, memberName, memberRole);

        // 응답 반환
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        System.out.println(response);
        return response;
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout() {

        // 로그아웃 성공 메세지
        return "로그아웃 성공";
    }

    // 테스트
    @GetMapping("/test")
    public String test(Principal principal) {
        // memberId만 뽑기
        // 수많은 토큰끼리 식별하기 위해 subject라는 것을 설정함(토큰의 이름표라고 보면 될듯)
        // 내가 subject(토큰의 이름표)를 memberId라고 설정함
        // 프린시펄은 로그인 한 사용자 식별자를 알 수 있음
        // 즉 프린시펄을 쓰면 subject로 설정한 memberId를 반환한다는 뜻
        String memberId = principal.getName();
        return memberId;

        // toString으로 전부 보기
        // String memberInfo = principal.toString();
        // return memberInfo;
    }
}
