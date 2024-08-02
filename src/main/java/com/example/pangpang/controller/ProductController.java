package com.example.pangpang.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public PageResponseDTO<ProductDTO> list(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "12") int size,
      @RequestParam(value = "search", required = false) String search) {

    // URL에서 전달받은 데이터 PageRequestDTO에 저장
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
        .page(page)
        .size(size)
        .search(search)
        .build();

    return productService.list(pageRequestDTO);
  }

  /* 상품 상세 보기 */
  @GetMapping("/read/{id}")
  public ProductDTO getDetail(@PathVariable(name = "id") Long id) {
    return productService.getDetail(id);
  }


}
