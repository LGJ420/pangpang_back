package com.example.pangpang.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.PageRequestDTO;
import com.example.pangpang.dto.PageResponseDTO;
import com.example.pangpang.dto.ProductDTO;
import com.example.pangpang.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

  private final ProductService productService;

  /* 상품 목록 보기 */
  @GetMapping("/list")
  public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
    return productService.list(pageRequestDTO);
  }


  /* 상품 상세 보기 */
  @GetMapping("/read/{id}")
  public ProductDTO getDetail(@PathVariable(name = "id")Long id) {
    return productService.getDetail(id);
  }

  
    
}
