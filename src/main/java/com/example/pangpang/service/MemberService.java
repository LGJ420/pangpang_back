package com.example.pangpang.service;

import java.security.Principal;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.naming.NameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pangpang.dto.MemberCheckIdInSignupDTO;
import com.example.pangpang.dto.MemberDTO;
import com.example.pangpang.dto.MemberInFindIdDTO;
import com.example.pangpang.dto.MemberInFindPwDTO;
import com.example.pangpang.dto.MemberInFindPwForResetDTO;
import com.example.pangpang.dto.MemberInLoginDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.exception.MemberNotFoundException;
import com.example.pangpang.repository.MemberRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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
    public Member findId(MemberInFindIdDTO memberInFindIdDTO) {
        // 회원 이름과 생년월일로 데이터베이스에서 회원 정보를 조회
        Member memberInfo = memberRepository.findByMemberNameAndMemberBirth(memberInFindIdDTO.getMemberNameInFindId(),
                memberInFindIdDTO.getMemberBirthInFindId());

        // 조회된 회원 정보가 없으면 예외 발생
        if (memberInfo == null) {
            throw new MemberNotFoundException("회원 정보를 찾을 수 없습니다");
        }

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

    // 로그인 서비스
    public Member login(MemberInLoginDTO memberInLoginDTO) {

        // 회원 존재 여부 확인 - 아이디
        Member member = memberRepository.findByMemberId(memberInLoginDTO.getMemberIdInLogin())
                .orElseThrow(() -> new MemberNotFoundException("아이디 혹은 비밀번호가 틀렸습니다."));

        // 회원 존재 여부 확인 - 비밀번호
        if (!passwordEncoder.matches(memberInLoginDTO.getMemberPwInLogin(), member.getMemberPw())) {
            throw new MemberNotFoundException("아이디 혹은 비밀번호가 틀렸습니다.");
        }

        // 회원 존재 여부 확인 - 아이디, 비밀번호 전부 확인 완료
        System.out.println("memberId, memberPw로 회원 존재 확인");

        Member memberInfo = memberRepository.findByMemberId(memberInLoginDTO.getMemberIdInLogin()).get();

        return memberInfo; // 아이디, 비밀번호 일치하는 멤버 반환
    }

    // ===================================================

}
