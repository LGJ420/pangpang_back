package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
