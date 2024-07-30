package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
    
}
