package com.example.pangpang.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.pangpang.dto.MemberDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    // ↑↑↑↑↑ 사용자의 인증 처리를 위해 인터페이스 임플리먼트

    // 멤버 리포지터리 의존성주입
    private final MemberRepository memberRepository;

    // 사용자 인정 처리를 위해 임플리먼트한 인터페이스의 기본 오버라이드 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        log.info("--------------------loadUserByUsername--------------------");

        Optional<Member> member = memberRepository.findByMemberId(username);

        if (member.isEmpty()) {
            throw new UsernameNotFoundException("회원을 찾지 못했습니다.");
        }

        MemberDTO memberDTO = new MemberDTO(
            member.get().getMemberId(),
            member.get().getMemberPw(),
            member.get().getMemberName(),
            member.get().getMemberBirth()
        );

        log.info(memberDTO);
        
        return memberDTO;
    }
}
