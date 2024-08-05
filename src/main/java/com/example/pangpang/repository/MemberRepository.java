package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    
    // 아이디 찾기
    // 멤버이름, 멤버생년월일이 필요함    
    Member findByMemberNameAndMemberBirth(String memberName, String memberBirth);
    
    // ===============================================================
    
    // 비밀번호 찾기
    // 멤버아이디, 멤버이름, 멤버생년월일이 필요함
    Member findByMemberIdAndMemberNameAndMemberBirth(String memberId, String memberName, String memberBirth);
    
    // ===============================================================
    
    // 로그인
    // 멤버 아이디, 멤버 비밀번호 필요함
    Optional<Member> findByMemberId(String memberId);
}