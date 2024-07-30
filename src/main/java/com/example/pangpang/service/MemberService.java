package com.example.pangpang.service;

import org.springframework.stereotype.Service;

import com.example.pangpang.dto.MemberDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
public class MemberService{
    // ↑↑↑↑↑ 사용자의 인증 처리를 위해 인터페이스 임플리먼트

    // 멤버 리포지터리 의존성주입
    private final MemberRepository memberRepository;


}
