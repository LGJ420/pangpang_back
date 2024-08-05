package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.OrdersProduct;

public interface OrdersProductRepository extends JpaRepository<OrdersProduct, Long> {
    
}
