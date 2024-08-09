package com.example.pangpang.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.*;

public interface CartRepository extends JpaRepository<Cart, Long>{

    List<Cart> findByMember(Member member);
    Optional<Cart> findByMemberAndProduct(Member member, Product product);

    void deleteByMemberAndProduct(Member member, Product product);
}
