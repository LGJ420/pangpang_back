package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.pangpang.entity.OrdersProduct;

public interface OrdersProductRepository extends JpaRepository<OrdersProduct, Long> {
    
  @Query("SELECT COALESCE(SUM(o.count), 0) FROM OrdersProduct o WHERE o.product.id = :productId")
    Integer getTotalSalesForProduct(@Param("productId") Long productId);
}
