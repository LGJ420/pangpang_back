package com.example.pangpang.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    public void checkMemberId(MemberDTO memberDTO) {

        // 아이디에 아무것도 안 적혀있을 때
        if (memberDTO.getMemberId().isBlank()) {
            throw new IllegalArgumentException("아이디는 공백일 수 없습니다.");
        }

        // 아이디 중복 확인
        if (memberRepository.findByMemberId(memberDTO.getMemberId()).isPresent()) {
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
    public Member findId(MemberDTO memberDTO) {
        Member memberInfo = memberRepository.findByMemberNameAndMemberBirth(
                memberDTO.getMemberName(),
                memberDTO.getMemberBirth())
                .orElseThrow(() -> new MemberNotFoundException("아이디 혹은 비밀번호가 틀렸습니다."));

        return memberInfo;
    }

    // ===================================================

    // 비밀번호 찾기 서비스
    public Member findPw(MemberDTO memberDTO) {
        // 회원 아이디, 회원 이름, 회원 생년월일로 데이터베이스에서 회원 정보를 조회
        Member memberInfo = memberRepository.findByMemberIdAndMemberNameAndMemberBirth(
                memberDTO.getMemberId(),
                memberDTO.getMemberName(),
                memberDTO.getMemberBirth())
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다.")); // 조회된 회원 정보가 없으면 예외 발생

        return memberInfo;
    }

    // 비밀번호 변경 서비스
    public void resetPw(MemberDTO memberDTO) {

        // 회원번호(id)로 회원 찾기
        Member existingMember = memberRepository
                .findByMemberId(memberDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

        // 비밀번호 암호화
        String encoderedPw = passwordEncoder.encode(memberDTO.getMemberPw());

        // 기존 엔티티 비밀번호만 변경
        existingMember.setMemberPw(encoderedPw);

        memberRepository.save(existingMember);
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
        Member memberInfo = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

        // 2. 입력된 비밀번호와 저장된 비밀번호 비교하기
        boolean checkPw = passwordEncoder.matches(memberPw, memberInfo.getMemberPw());

        // 2-1. [입력된 비밀번호 ≠ 저장된 비밀번호] => 예외처리
        if (!checkPw) {
            throw new MemberNotFoundException("비밀번호 일치하지 않음");
        }

        // 3. [입력된 비밀번호 = 저장된 비밀번호] => 컨트롤러에서 try 실행
        return memberInfo;
    }

    // =========================================================

    // 내 정보 변경(수정)
    public void modifyProfile(String memberId, MemberDTO memberDTO) {
        // 1. 로그인된 사용자 찾기
        Member modifyMember = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member Not Found"));

        // 2. 내 정보 수정 진행
        modifyMember.setMemberNickname(memberDTO.getMemberNickname());
        modifyMember.setMemberPhone(memberDTO.getMemberPhone());
        modifyMember.setPostcode(memberDTO.getPostcode());
        modifyMember.setPostAddress(memberDTO.getPostAddress());
        modifyMember.setDetailAddress(memberDTO.getDetailAddress());
        modifyMember.setExtraAddress(memberDTO.getExtraAddress());

        // 2-1. 비밀번호가 변경된 경우에만 암호화하여 저장
        if (memberDTO.getMemberPw() != null) {
            // 비밀번호 암호화
            String encoderedPw = passwordEncoder.encode(memberDTO.getMemberPw());
            modifyMember.setMemberPw(encoderedPw);
        }

        memberRepository.save(modifyMember);

    }

    // 관리자-회원관리 리스트 받아오기
    public List<MemberDTO> manageList() {
        List<Member> members = memberRepository.findAll();

        // 1. members 리스트를 스트림으로 변환
        // 2. 각 Member객체를 MemberDTO로 매핑
        List<MemberDTO> memberDTOs = members.stream().map(member -> {
            MemberDTO memberDTO = MemberDTO.builder()
                    .id(member.getId())
                    .memberId(member.getMemberId())
                    .memberName(member.getMemberName())
                    .memberNickname(member.getMemberNickname())
                    .memberRole(member.getMemberRole())
                    .memberSignupDate(member.getMemberSignupDate())
                    .isActive(member.isActive())
                    .build();
            return memberDTO;
        }).collect(Collectors.toList()); // 스트림의 결과를 다시 리스트 형태로 수집

        return memberDTOs;
    }

    // 관리자-회원관리 회원등급 변경
    public void changeMemberRole(Long id, String memberRole) {
        // 회원찾기
        Member extistingMember = memberRepository.findById(id)
        .orElseThrow(()->new MemberNotFoundException("회원을 찾을 수 없습니다."));

        // 회원 등급 변경
        extistingMember.setMemberRole(memberRole);

        // 변경 내용 저장
        memberRepository.save(extistingMember);
    }
}
