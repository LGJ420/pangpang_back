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

  @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImage WHERE p.id = :id")
  Optional<Product> findProductWithImages(@Param("id") Long id);

  // 상세 보기
  @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImage WHERE p.id = :id")
  Optional<Product> selectOne(@Param("id") Long id);

  // 목록 보기
  @Query("select p, pi from Product p left join p.productImage pi")
  Page<Product> selectList(Pageable pageable);

  @Query("select p from Product p where p.productTitle like %:search%")
  Page<Product> findByProductTitleContainingWithImage(@Param("search") String search, Pageable pageable);

  // 카테고리별 정렬
  @Query("SELECT p FROM Product p WHERE (:category IS NULL OR :category = '' OR p.productCategory = :category) " +
      "AND (:search IS NULL OR :search = '' OR p.productTitle LIKE %:search%) " + "AND p.productStock > 0" +
      "ORDER BY p.id DESC")
  Page<Product> findProductsByCategoryAndSearch(@Param("category") String category,
      @Param("search") String search,
      Pageable pageable);

  @Query("SELECT p FROM Product p ORDER BY p.id DESC")
  Page<Product> findAllProducts(@Param("category") String category,
      Pageable pageable);

  // 상품 랜덤으로 가져오기 (메인에서 사용)
  @Query("SELECT p FROM Product p ORDER BY RAND() LIMIT 3")
  List<Product> findAllRandom();

  // 특정 Product ID들에 대한 ProductImage 조회
  @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id IN :productIds")
  List<ProductImage> findImagesByProductIds(@Param("productIds") List<Long> productIds);

}
