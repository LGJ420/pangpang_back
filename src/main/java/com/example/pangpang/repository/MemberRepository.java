package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByMemberId(String memberId);

    // 아이디 찾기에 멤버이름, 멤버생년월일이 필요함
    public boolean existsByMemberNameAndMemberBirth(String memberName, String memberBirth);

    Member findByMemberNameAndMemberBirth(String memberName, String memberBirth);

    // ===============================================================
    
    // 비밀번호 찾기에 멤버아이디, 멤버이름, 멤버생년월일이 필요함
    public boolean existsByMemberIdAndMemberNameAndMemberBirth(String memberId, String memberName, String memberBirth);
    
    Member findByMemberIdAndMemberNameAndMemberBirth(String memberId, String memberName, String memberBirth);
    
    // ===============================================================
    
    // 로그인에 멤버 아이디, 멤버 비밀번호 필요함
    Optional<Member> findByMemberIdAndMemberPw(String memberId, String MemberPw);

    // 로그인에 멤버 아이디, 멤버 비밀번호 유무 알기 위함
    public boolean existsByMemberIdAndMemberPw(String memberId, String memberPw);
}