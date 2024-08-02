package com.example.pangpang.service;

import java.util.Optional;

import org.hibernate.mapping.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pangpang.dto.MemberDTO;
import com.example.pangpang.dto.MemberInFindIdDTO;
import com.example.pangpang.dto.MemberInFindPwDTO;
import com.example.pangpang.dto.MemberInFindPwForResetDTO;
import com.example.pangpang.dto.MemberInLoginDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
public class MemberService {
    // ↑↑↑↑↑ 사용자의 인증 처리를 위해 인터페이스 임플리먼트

    // 멤버 리포지터리 의존성주입
    private final MemberRepository memberRepository;

    // 암호화 의존성 주입
    private final PasswordEncoder passwordEncoder;

    // ===================================================
    
    // 회원가입 서비스
    public void createMember(MemberDTO memberDTO) {
        
        // 비밀번호 암호화
        String encoderedPw = passwordEncoder.encode(memberDTO.getMemberPw());
        
        // 리액트 입력값 -> 엔티티 등록값 변경(매핑비스무리)
        Member member = Member.builder()
        .memberId(memberDTO.getMemberId())
        // 비밀번호만 암호화 된 거 사용
        .memberPw(encoderedPw)
        .memberName(memberDTO.getMemberName())
        .memberBirth(memberDTO.getMemberBirth())
        .memberRole(memberDTO.getMemberRole())
        .build();
        
        memberRepository.save(member);
    }

    // ===================================================
    
    // 아이디 찾기 서비스
    public Optional<Member> findId(MemberInFindIdDTO memberInFindIdDTO) {
        // 리액트 입력값을 레포지토리를 통해 데이터 확인
        Optional<Member> memberInfo = memberRepository.findByMemberNameAndMemberBirth(
            memberInFindIdDTO.getMemberNameInFindId(),
            memberInFindIdDTO.getMemberBirthInFindId());
            
            // 위에서 데이터를 확인했을 때 데이터의 유무 확인
            if (memberInfo.isPresent()) {
                return Optional.of(memberInfo.get());
            } else {
                return Optional.empty();
            }
        }
        
    // ===================================================
    
    // 비밀번호 찾기 서비스
    public Optional<Member> findPw(MemberInFindPwDTO memberInFindPwDTO) {
        // 리액트 입력값을 레포지토리를 통해 데이터 확인
        Optional<Member> memberInfo = memberRepository.findByMemberIdAndMemberNameAndMemberBirth(
            memberInFindPwDTO.getMemberIdInFindPw(),
            memberInFindPwDTO.getMemberNameInFindPw(),
            memberInFindPwDTO.getMemberBirthInFindPw());
            
            // 위에서 데이터를 확인했을 때 데이터의 유무 확인
    if (memberInfo.isPresent()) {
        return Optional.of(memberInfo.get());
    } else {
        return Optional.empty();
    }
    }

    // 비밀번호 변경 서비스
    public void resetPw(MemberInFindPwForResetDTO memberInFindPwResetForDTO) {

        // 회원번호(id)로 회원 존재 유무 확인
        Member existingMember = memberRepository.findById(memberInFindPwResetForDTO.getIdInFindPwForReset())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 비밀번호 암호화
        String encoderedPw = passwordEncoder.encode(memberInFindPwResetForDTO.getMemberPwInFindPwForReset());

        // 기존 엔티티 비밀번호만 변경
        // 세터를 쓴 이유 : 빌더를 쓰면 id, memberId, memberPw... 등 다 적어야함
        // 귀찮아서 세터씀 ^^)>
        existingMember.setMemberPw(encoderedPw);

        memberRepository.save(existingMember);
    }

    // ===================================================

    // 로그인 서비스
}
