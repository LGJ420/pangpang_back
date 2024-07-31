package com.example.pangpang.controller;

import java.util.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.MemberDTO;
import com.example.pangpang.dto.MemberInFindIdDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public Map<String, String> signup(@Valid @RequestBody MemberDTO memberDTO) {

        memberService.createMember(memberDTO);

        return Map.of("result", "회원가입 성공!", "memberName", memberDTO.getMemberName());
    }

    // 아이디 찾기
    @PostMapping(value = "/find_id")
    public ResponseEntity<?> findId(@Valid @RequestBody MemberInFindIdDTO memberInFindIdDTO) {

        Optional<Member> memberInfo = memberService.findId(memberInFindIdDTO);

        if (memberInfo.isPresent()) {
            return ResponseEntity.ok(memberInfo.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디를 찾을 수 없습니다.");
        }
    }

    // 비밀번호 찾기

}
