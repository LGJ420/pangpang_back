package com.example.pangpang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.pangpang.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
  /* 상품명 기준으로 검색 */
  Page<Product> findByProductTitleContaining(String search, Pageable pageable);


  /* 상품 랜덤으로 가져오기 (메인에서 사용) */
  @Query(value = "SELECT * FROM product ORDER BY RAND()", nativeQuery = true)
  Page<Product> findAllRandom(Pageable pageable);
}
