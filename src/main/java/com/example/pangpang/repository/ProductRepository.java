package com.example.pangpang.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pangpang.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {


  @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImage WHERE p.id = :id")
  Optional<Product> findProductWithImages(@Param("id") Long id);


  // 상세 보기 - 상품 이미지 포함
  // 주어진 id에 해당하는 Product 조회. 조회 결과에는 ProductImage도 포함
  @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImage WHERE p.id = :id")
  Optional<Product> selectOne(@Param("id") Long id);


  
  // 목록 보기 - 상품 이미지 포함
  // 모든 Product와 각 Product에 연결된 ProductImage도 함께 조회. 결과는 페이지로 반환
  @Query("select p, pi from Product p left join p.productImage pi")
  Page<Product> selectList(Pageable pageable);



  @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImage WHERE p.productCategory = :category ORDER BY p.id DESC")
  Page<Product> findByProductCategory(@Param("category") String category, Pageable pageable);

  // 상품명 기준으로 검색
  // 조회된 Product들과 각 Product에 연결된 ProductImage를 함꼐 로딩하여 페이지로 반환
  @Query("select p, pi from Product p left join p.productImage pi where p.productTitle like %:search%")
  Page<Product> findByProductTitleContainingWithImage(@Param("search") String search, Pageable pageable);


  @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImage " +
      "WHERE (:category IS NULL OR :category = '' OR p.productCategory = :category) " +
      "AND (:search IS NULL OR :search = '' OR p.productTitle LIKE %:search%) " +
      "ORDER BY p.id DESC")
  Page<Product> findByCategoryAndSearch(@Param("category") String category,
      @Param("search") String search,
      Pageable pageable);

  // 상품 랜덤으로 가져오기 (메인에서 사용)
  @Query("SELECT p, pi FROM Product p LEFT JOIN p.productImage pi ORDER BY RAND() LIMIT 3")
  List<Product> findAllRandomWithImages();

}
