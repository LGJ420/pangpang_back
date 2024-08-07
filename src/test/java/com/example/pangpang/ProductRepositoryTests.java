package com.example.pangpang;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.pangpang.entity.Product;
import com.example.pangpang.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

  @Autowired
  ProductRepository productRepository;

  @Test
  public void testInsert() {
    for (int i = 0; i < 5; i++) {
      Product product = Product.builder()
          .productTitle("상품 " + i)
          .productPrice(i * 1000)
          .productContent("상품 설명 " + i).build();

          
    }
  }

}
