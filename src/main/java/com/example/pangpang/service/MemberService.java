package com.example.pangpang.service;

import org.hibernate.mapping.Map;
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

    // 회원가입 서비스
    public void createMember(MemberDTO memberDTO){

        // 리액트 입력값 -> 엔티티 등록값 변경(매핑비스무리)
        Member member = Member.builder()
            .memberId(memberDTO.getMemberId())
            .memberPw(memberDTO.getMemberPw())
            .memberName(memberDTO.getMemberName())
            .memberBirth(memberDTO.getMemberBirth())
            .memberRole(memberDTO.getMemberRole())
            .build();

        memberRepository.save(member);
    }

}
