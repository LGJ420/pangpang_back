package com.example.pangpang.controller;

import java.util.*;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.ProductDTO;

import com.example.pangpang.service.ProductService;
import com.example.pangpang.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;

/* 메인페이지에 사용하는 컨트롤러 입니다 */

@RestController
@RequiredArgsConstructor
public class MainPageController {

  private final ProductService productService;
    private final CustomFileUtil fileUtil;

  @GetMapping("/")
  public List<ProductDTO> mainList() {

    return productService.mainList();
  }


  /* 이미지 조회 */
  @GetMapping("/view/{fileName}")
  public ResponseEntity<Resource> viewFileGET(@PathVariable(name = "fileName") String fileName) {
    return fileUtil.getFile(fileName);
  }

}
