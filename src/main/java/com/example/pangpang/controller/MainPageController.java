package com.example.pangpang.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.ProductDTO;
import com.example.pangpang.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainPageController {
  

  private final ProductService productService;

  @GetMapping("/")
  public List<ProductDTO> mainProductList() {
      return productService.mainList();
  }
  
}
