package com.example.pangpang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Member;
import com.example.pangpang.entity.Orders;
import com.example.pangpang.entity.Product;

public interface OrdersRepository extends JpaRepository<Orders, Long>{
    
    Optional<Orders> findByMemberAndProduct(Member member, Product product);
}
