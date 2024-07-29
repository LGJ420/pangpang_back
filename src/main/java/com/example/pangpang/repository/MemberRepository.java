package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Member;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByMemberId(String memberId);
}