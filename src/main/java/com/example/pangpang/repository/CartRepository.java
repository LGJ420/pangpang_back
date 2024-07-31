package com.example.pangpang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.*;

public interface CartRepository extends JpaRepository<Cart, Long>{

    Optional<Cart> findByMemberAndProduct(Member member, Product product);

    void deleteByMemberAndProduct(Member member, Product product);
}
