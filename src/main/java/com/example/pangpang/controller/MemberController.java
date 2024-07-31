package com.example.pangpang.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.MemberDTO;
import com.example.pangpang.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

}
