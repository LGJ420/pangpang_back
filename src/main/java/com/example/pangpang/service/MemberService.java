package com.example.pangpang.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.Member;
import com.example.pangpang.exception.MemberNotFoundException;
import com.example.pangpang.repository.MemberRepository;

import java.security.Principal;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {
    // ↑↑↑↑↑ 사용자의 인증 처리를 위해 인터페이스 임플리먼트

    // 멤버 리포지터리 의존성주입
    private final MemberRepository memberRepository;

    // 암호화 의존성 주입
    private final PasswordEncoder passwordEncoder;

    // ===================================================

    // 아이디 중복 확인
    public void checkMemberId(MemberCheckIdInSignupDTO memberCheckIdInSignupDTO) {

        // 아이디에 아무것도 안 적혀있을 때
        if (memberCheckIdInSignupDTO.getMemberId().isBlank()) {
            throw new IllegalArgumentException("아이디는 공백일 수 없습니다.");
        }

        // 아이디 중복 확인
        Optional<Member> memberIdCheck = memberRepository.findByMemberId(memberCheckIdInSignupDTO.getMemberId());

        // 아이디가 존재할 때
        if (memberIdCheck.isPresent()) {
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");
        }
    }

    // 회원가입 서비스
    public void createMember(MemberDTO memberDTO) {

        // 비밀번호 암호화
        String encoderedPw = passwordEncoder.encode(memberDTO.getMemberPw());

        // 리액트 입력값 -> 엔티티 등록값 변경(매핑비스무리)
        Member member = Member.builder()
                .memberId(memberDTO.getMemberId())
                .memberPw(encoderedPw) // 비밀번호만 암호화 된 거 사용
                .memberName(memberDTO.getMemberName())
                .memberNickname(memberDTO.getMemberNickname())
                .memberBirth(memberDTO.getMemberBirth())
                .memberPhone(memberDTO.getMemberPhone())
                .postcode(memberDTO.getPostcode()) // 우편번호
                .postAddress(memberDTO.getPostAddress()) // 경기도 성남시 어쩌고저쩌고
                .detailAddress(memberDTO.getDetailAddress()) // 101동 505호
                .extraAddress(memberDTO.getExtraAddress()) // (미금동)
                .memberRole(memberDTO.getMemberRole()) // Admin, User
                .memberSignupDate(LocalDateTime.now()) // 회원가입 시간
                .build();

        memberRepository.save(member);
    }

    // ===================================================

    // 아이디 찾기 서비스
    public Optional<Member> findId(MemberInFindIdDTO memberInFindIdDTO) {
        Optional<Member> memberInfo = memberRepository.findByMemberNameAndMemberBirth(
                memberInFindIdDTO.getMemberNameInFindId(),
                memberInFindIdDTO.getMemberBirthInFindId());

        return memberInfo;
    }

    // ===================================================

    // 비밀번호 찾기 서비스
    public Member findPw(MemberInFindPwDTO memberInFindPwDTO) {
        // 회원 아이디, 회원 이름, 회원 생년월일로 데이터베이스에서 회원 정보를 조회
        Member memberInfo = memberRepository.findByMemberIdAndMemberNameAndMemberBirth(
                memberInFindPwDTO.getMemberIdInFindPw(),
                memberInFindPwDTO.getMemberNameInFindPw(),
                memberInFindPwDTO.getMemberBirthInFindPw());

        // 조회된 회원 정보가 없으면 예외 발생
        if (memberInfo == null) {
            throw new MemberNotFoundException("회원 정보를 찾을 수 없습니다.");
        }

        return memberInfo;
    }

    // 비밀번호 변경 서비스
    public void resetPw(MemberInFindPwForResetDTO memberInFindPwResetForDTO) {

        // 회원번호(id)로 회원 찾기
        Optional<Member> existingMemberOptional = memberRepository
                .findById(memberInFindPwResetForDTO.getIdInFindPwForReset());

        if (existingMemberOptional.isPresent()) {
            Member existingMember = existingMemberOptional.get();
            // 비밀번호 암호화
            String encoderedPw = passwordEncoder.encode(memberInFindPwResetForDTO.getMemberPwInFindPwForReset());

            // 기존 엔티티 비밀번호만 변경
            // 세터를 쓴 이유 : 빌더를 쓰면 id, memberId, memberPw... 등 다 적어야함
            // 귀찮아서 세터씀 ^^)>
            existingMember.setMemberPw(encoderedPw);

            memberRepository.save(existingMember);
        } else {
            throw new MemberNotFoundException("회원 정보를 찾을 수 없습니다.");
        }

    }

    // ===================================================

    // // 로그인 서비스
    // 토큰을 만들기 위해 로그인에서 findByMemberId가 필요함
    public Member findByMemberId(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("아이디 혹은 비밀번호가 틀렸습니다."));

        return member;
    }

    // 마이페이지 내정보변경 전 비밀번호 확인
    public Member confirmBeforeProfile(String memberId, String memberPw) {

        // 로그인된 사용자의 비밀번호와 입력된 비밀번호 비교하기
        // 1. 로그인된 사용자 찾기
        Optional<Member> memberInfo = memberRepository.findByMemberId(memberId);

        // 2. 사용자가 존재하지 않으면 예외 처리(예외가 뜰리가 없음...!)
        if (memberInfo.isEmpty()) {
            throw new MemberNotFoundException("회원 정보를 찾을 수 없습니다.");
        }

        // 3. 사용자 GET
        Member member = memberInfo.get();

        // 4. 입력된 비밀번호와 저장된 비밀번호 비교하기
        boolean checkPw = passwordEncoder.matches(memberPw, member.getMemberPw());

        if (checkPw) {
            return member;
        } else {
            throw new MemberNotFoundException("비밀번호 일치하지 않음");
        }
    }
}
