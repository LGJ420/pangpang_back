package com.example.pangpang.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.ProductDTO;

import com.example.pangpang.service.ProductService;

import lombok.RequiredArgsConstructor;

/* 메인페이지에 사용하는 상품 목록 리스트 입니다 */

@RestController
@RequiredArgsConstructor
public class MainPageController {
  

  private final ProductService productService;

  @GetMapping("/")
  public List<ProductDTO> mainProductList() {
      return productService.mainList();
  }
  
}
