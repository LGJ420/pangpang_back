package com.example.pangpang.controller;

import java.util.*;
import java.security.Principal;

import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.Member;
import com.example.pangpang.exception.MemberNotFoundException;
import com.example.pangpang.service.MemberService;
import com.example.pangpang.util.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final AuthenticationManager authenticationManager;

    private final MemberService memberService;

    private final JwtUtil jwtUtil;

    // 회원가입 - 아이디 중복 확인
    @PostMapping("/signup/checkMemberId")
    public Map<String, String> chechMemberId(@RequestBody MemberDTO memberDTO) {

        memberService.checkMemberId(memberDTO);

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
    public ResponseEntity<Member> findId(@Valid @RequestBody MemberInFindIdDTO memberInFindIdDTO) {
        Member memberInfo = memberService.findId(memberInFindIdDTO)
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));
        return ResponseEntity.ok(memberInfo);
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
    public ResponseEntity<String> login(@RequestBody MemberInLoginDTO memberInLoginDTO) {
        try {
            // authenticationManager는 스프링 시큐리티에서 제공하는 객체
            // 사용자의 자격 증명을 검증하는 역할
            // authenticate()은 전달된 자격 증명이 유효한지 확인
            // => ★요약 : 굳이 로그인 컨트롤러에 로그인 과정을 직접 만들 필요가 없다는 소리★
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    memberInLoginDTO.getMemberIdInLogin(), memberInLoginDTO.getMemberPwInLogin()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member member = memberService.findByMemberId(memberInLoginDTO.getMemberIdInLogin());
            String jwt = jwtUtil.generateToken(
                    member.getMemberId(),
                    member.getId(),
                    member.getMemberName(),
                    member.getMemberNickname(),
                    member.getMemberRole());
            return ResponseEntity.ok(jwt);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials : 잘못된 자격증명");
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout() {

        // 로그아웃 성공 메세지
        return "로그아웃 성공";
    }

    // 마이페이지에서 내정보변경할때, 비밀번호로 한번 확인
    @PostMapping("/confirm_before_profile")
    public ResponseEntity<?> confirmBeforeProfile(Principal principal,
            @RequestBody MemberDTO memberDTO) {

        // 현재 로그인된 사용자 정보 가져오기
        String loginedMemberId = principal.getName();

        // 로그인된 사용자의 비밀번호 = 입력된 비밀번호 값 따짐
        try {
            Member member = memberService.confirmBeforeProfile(loginedMemberId,
                    memberDTO.getMemberPw());
            // 참이면 return Repository.ok(해당 멤버의 entity 값 전송)
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            // 거짓이면 에러메세지 띄움
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/mypage/modify")
    public ResponseEntity<String> modifyProfile(Principal principal, @RequestBody MemberDTO memberDTO) {

        // 현재 로그인된 사용자 정보 가져오기
        String loginedMemberId = principal.getName();

        try {
            memberService.modifyProfile(loginedMemberId, memberDTO);

            Member member = memberService.findByMemberId(loginedMemberId);
            String jwt = jwtUtil.generateToken(
                    member.getMemberId(),
                    member.getId(),
                    member.getMemberName(),
                    member.getMemberNickname(),
                    member.getMemberRole());
            return ResponseEntity.ok(jwt);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }

    }

    // 테스트
    @GetMapping("/test")
    public String test(Principal principal) {
        // memberId만 뽑기
        // 수많은 토큰끼리 식별하기 위해 subject라는 것을 설정함(토큰의 이름표라고 보면 될듯)
        // 내가 subject(토큰의 이름표)를 memberId라고 설정함
        // 프린시펄은 로그인 한 사용자 식별자를 알 수 있음
        // 즉 프린시펄을 쓰면 subject로 설정한 memberId를 반환한다는 뜻
        // String memberId = principal.getName();
        // return memberId;

        // toString으로 전부 보기
        String memberInfo = principal.toString();
        return memberInfo;
    }
}
