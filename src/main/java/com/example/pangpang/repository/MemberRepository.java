package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByMemberId(String memberId);

    // 아이디 찾기에 멤버이름, 멤버생년월일이 필요함
    Optional<Member> findByMemberNameAndMemberBirth(String memberName, String memberBirth);

    // 비밀번호 찾기에 멤버아이디, 멤버이름, 멤버생년월일이 필요함
    Optional<Member> findByMemberIdAndMemberNameAndMemberBirth(String memberId, String memberName, String memberBirth);
}