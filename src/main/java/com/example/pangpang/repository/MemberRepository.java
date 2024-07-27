package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

    
}