package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long>{
    
    
}
