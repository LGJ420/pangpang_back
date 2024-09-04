package com.example.pangpang.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pangpang.entity.Product;
import com.example.pangpang.entity.ProductImage;

public interface ProductRepository extends JpaRepository<Product, Long> {

  // 관리 페이지 상품 목록
  @Query("select p from Product p where p.productTitle like %:search%")
  Page<Product> findByProductTitleContainingWithImage(@Param("search") String search, Pageable pageable);

  // 쇼핑 페이지 상품 목록
  @Query("SELECT p FROM Product p WHERE (:category IS NULL OR :category = '' OR p.productCategory = :category) " +
      "AND (:search IS NULL OR :search = '' OR p.productTitle LIKE %:search%) " +
      "ORDER BY p.id DESC")
  Page<Product> findProductsByCategoryAndSearch(@Param("category") String category,
      @Param("search") String search,
      Pageable pageable);
      

  // 상품 랜덤으로 가져오기 (메인에서 사용)
  @Query("SELECT p FROM Product p ORDER BY RAND() LIMIT 3")
  List<Product> findAllRandom();

  // 특정 Product ID들에 대한 ProductImage 조회
  @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id IN :productIds")
  List<ProductImage> findImagesByProductIds(@Param("productIds") List<Long> productIds);

}
