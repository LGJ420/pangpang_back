package com.example.pangpang.controller;

import java.util.*;
import java.security.Principal;

import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.MemberService;
import com.example.pangpang.util.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
    public ResponseEntity<String> checkMemberId(@RequestBody MemberDTO memberDTO) {

        try {
            memberService.checkMemberId(memberDTO);
            return ResponseEntity.ok("아이디 중복 확인 성공");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody MemberDTO memberDTO) {

        try {
            memberService.createMember(memberDTO);
            return ResponseEntity.ok().body("회원가입 성공");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

    // 아이디 찾기
    @PostMapping("/find_id")
    public ResponseEntity<?> findId(@RequestBody MemberDTO memberDTO) {

        try {
            Member memberInfo = memberService.findId(memberDTO);
            return ResponseEntity.ok(memberInfo);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

    // 비밀번호 찾기
    @PostMapping("/find_pw")
    public ResponseEntity<?> findPw(@RequestBody MemberDTO memberDTO) {

        try {
            Member memberInfo = memberService.findPw(memberDTO);
            return ResponseEntity.ok().body(memberInfo);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

    // 비밀번호 찾기->비밀번호 변경
    @PostMapping("/find_pw/reset")
    public ResponseEntity<?> resetPw(@RequestBody MemberDTO memberDTO) {

        try {
            memberService.resetPw(memberDTO);
            return ResponseEntity.ok("비밀번호 변경 성공!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO memberDTO) {
        try {
            // authenticationManager는 스프링 시큐리티에서 제공하는 객체
            // 사용자의 자격 증명을 검증하는 역할
            // authenticate()은 전달된 자격 증명이 유효한지 확인
            // => ★요약 : 굳이 로그인 컨트롤러에 로그인 과정을 직접 만들 필요가 없다는 소리★
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    memberDTO.getMemberId(), memberDTO.getMemberPw()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member member = memberService.findByMemberId(memberDTO.getMemberId());
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

    // 마이페이지 내정보변경
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

    // 마이페이지 관리자 회원관리 회원리스트 받아오기
    @GetMapping
    public ResponseEntity<List<MemberDTO>> manageList() {

        List<MemberDTO> memberDTOs = memberService.manageList();

        return ResponseEntity.ok().body(memberDTOs);
    }

    // 마이페이지 관리자 회원관리 회원등급 변경
    @PostMapping("/mypage/manager/change/role")
    public ResponseEntity<?> changeMemberRole(@RequestBody MemberDTO memberDTO) {

        try {
            memberService.changeMemberRole(memberDTO.getId(), memberDTO.getMemberRole());
            return ResponseEntity.ok().body("회원번호 : " + memberDTO.getId() + " 회원등급 : " + memberDTO.getMemberRole());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

    // 마이페이지 관리자 회원관리 회원등급 변경
    @PostMapping("/mypage/manager/change/isActive")
    public ResponseEntity<?> changeIsActive(@RequestBody MemberDTO memberDTO) {

        System.out.println("프론트에서 전달받은 active : " + memberDTO.isActive());
        
        try {
            memberService.changeIsActive(memberDTO.getId(), memberDTO.isActive());
            return ResponseEntity.ok().body("회원번호 : " + memberDTO.getId() + " 변경 후 회원활동상태 : " + memberDTO.isActive());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }
}
