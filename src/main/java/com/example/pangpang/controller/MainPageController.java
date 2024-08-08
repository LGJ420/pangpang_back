package com.example.pangpang.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.PageRequestDTO;
import com.example.pangpang.dto.PageResponseDTO;
import com.example.pangpang.dto.ProductDTO;

import com.example.pangpang.service.ProductService;

import lombok.RequiredArgsConstructor;

/* 메인페이지에 사용하는 컨트롤러 입니다 */

@RestController
@RequiredArgsConstructor
public class MainPageController {

  private final ProductService productService;

  @GetMapping("/")
  public PageResponseDTO<ProductDTO> mainList(@RequestParam(value = "size", defaultValue = "3") int size) {

    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
        .size(size)
        .build();
    return productService.mainList(pageRequestDTO);
  }

}
