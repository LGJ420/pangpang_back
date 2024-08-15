package com.example.pangpang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.ProductReview;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>{
    
    boolean existsByProductIdAndMemberId(Long productId, Long memberId);

    List<ProductReview> findByProductId(Long productId);

    List<ProductReview> findByMemberId(Long memberId);
}
