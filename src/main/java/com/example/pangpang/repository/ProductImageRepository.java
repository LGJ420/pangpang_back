package com.example.pangpang.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

  // 주어진 상품 id 목록에 해당하는 productImage 엔티티 모두 조회
  List<ProductImage> findByProductIdIn(List<Long> productIds);

  // productId에 해당하는 모든 ProductImage를 조회합니다.
  List<ProductImage> findByProductId(Long productId);
}
