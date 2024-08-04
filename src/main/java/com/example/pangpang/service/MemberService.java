package com.example.pangpang.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    // 비밀 키 (비밀 키는 환경 변수나 설정 파일에서 관리하는 것이 좋습니다)
    private final String secretKey = "your_secret_key";
    private final long expirationTime = 86400000; // 1일 (밀리초 단위)

    // 로그인 서비스
    public String login(MemberInLoginDTO memberInLoginDTO) {

        // 비밀 키
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 회원 존재 여부 확인 - 아이디
        Member member = memberRepository.findByMemberId(memberInLoginDTO.getMemberIdInLogin())
                .orElseThrow(() -> new MemberNotFoundException("아이디 혹은 비밀번호가 틀렸습니다."));

        // 회원 존재 여부 확인 - 비밀번호
        if (!passwordEncoder.matches(memberInLoginDTO.getMemberPwInLogin(), member.getMemberPw())) {
            throw new MemberNotFoundException("아이디 혹은 비밀번호가 틀렸습니다.");
        }

        // 회원 존재 여부 확인 - 아이디, 비밀번호 전부 확인 완료
        System.out.println("memberId, memberPw로 회원 존재 확인");

        // // memberId, memberPw로 회원이 존재하면 로그인하기
        // 회원 인증 성공 후 JWT 생성
        String token = Jwts.builder() // 1. JWT 생성을 위한 빌더 초기화
                .setSubject(memberInLoginDTO.getMemberIdInLogin()) // 2. JWT 주제 설정(사용자 식별)
                .setIssuedAt(new Date()) // 3. JWT 발급 시간 설정(현재 시간)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 4. JWT 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 5. JWT 서명 추가(무결성 보장)
                .compact(); // 6. 설정된 JWT를 압축

        return token; // 생성된 JWT 반환
    }
}
