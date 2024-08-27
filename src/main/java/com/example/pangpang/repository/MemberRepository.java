package com.example.pangpang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pangpang.entity.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 아이디 찾기
    // 멤버이름, 멤버생년월일이 필요함
    Optional<Member> findByMemberNameAndMemberBirth(String memberName, String memberBirth);

    // ===============================================================

    // 비밀번호 찾기
    // 멤버아이디, 멤버이름, 멤버생년월일이 필요함
    Optional<Member> findByMemberIdAndMemberNameAndMemberBirth(String memberId, String memberName, String memberBirth);

    // ===============================================================

    // 회원가입 - 아이디 중복
    // 로그인
    // 멤버 아이디, 멤버 비밀번호 필요함
    Optional<Member> findByMemberId(String memberId);

    // ===============================================================

    // 회원 아이디 기준으로 검색
    // 조회된 member를 로딩하여 페이지로 반환
    @Query("select m from Member m where m.memberId like %:search%")
    Page<Member> findByMemberIdForSearch(@Param("search") String search, Pageable pageable);
}