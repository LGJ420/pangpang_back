package com.example.pangpang.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.ProductReview;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>{
    
    boolean existsByProductIdAndMemberId(Long productId, Long memberId);

    List<ProductReview> findByProductId(Long productId, Sort sort);

    List<ProductReview> findByMemberId(Long memberId, Sort sort);
}
