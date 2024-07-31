package com.example.pangpang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
  /* 상품명 기준으로 검색 */
  Page<Product> findByProductTitleContaining(String search, Pageable pageable);
}
