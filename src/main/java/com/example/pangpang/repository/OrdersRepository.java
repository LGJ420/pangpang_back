package com.example.pangpang.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Member;
import com.example.pangpang.entity.Orders;


public interface OrdersRepository extends JpaRepository<Orders, Long>{
    
    List<Orders> findByMember(Member member);
}
