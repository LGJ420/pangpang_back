package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

  
}
