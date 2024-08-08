package com.example.pangpang.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pangpang.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{


  /* 상세 보기 - 상품 이미지 포함 */
  @EntityGraph(attributePaths = "productImage")
  @Query("select p from Product p where p.id = :id")
  Optional<Product> selectOne(@Param("id") Long id);

  /* 목록 보기 - 상품 이미지 포함 */
  @Query("select p, pi from Product p left join p.productImage pi")
  Page<Object[]> selectList(Pageable pageable);


  /* 상품명 기준으로 검색하고, 이미지도 함께 로딩 */
  @Query("select p, pi from Product p left join p.productImage pi where p.productTitle like %:search%")
  Page<Object[]> findByProductTitleContainingWithImage(@Param("search") String search, Pageable pageable);

    
  /* 상품명 기준으로 검색 */
  Page<Product> findByProductTitleContaining(String search, Pageable pageable);


  /* 상품 랜덤으로 가져오기 (메인에서 사용) */
  @Query(value = "SELECT * FROM product ORDER BY RAND()", nativeQuery = true)
  Page<Product> findAllRandom(Pageable pageable);
}
