package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.ProductReview;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>{
    
}
