package com.example.pangpang.controller;

import java.util.*;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.pangpang.dto.*;
import com.example.pangpang.service.ProductService;
import com.example.pangpang.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/product")
public class ProductController {

  private final ProductService productService;
  private final CustomFileUtil fileUtil;

  /* 상품 등록 */
  @PostMapping("/add")
  public ResponseEntity<Long> addProduct(
      @RequestParam("productTitle") String productTitle,
      @RequestParam("productContent") String productContent,
      @RequestParam("productPrice") int productPrice,
      @RequestParam("files") List<MultipartFile> files) {

    // DTO 생성
    ProductDTO productDTO = ProductDTO.builder()
        .productTitle(productTitle)
        .productContent(productContent)
        .productPrice(productPrice)
        .files(files)
        .build();



    // 상품 등록 서비스 호출
    Long productId = productService.addProduct(productDTO);

    // 성공적인 응답 반환
    return ResponseEntity.status(HttpStatus.CREATED).body(productId);
  }


  /* 이미지 조회 */
  @GetMapping("/view/{fileName}")
  public ResponseEntity<Resource> viewFileGET(@PathVariable(name = "fileName") String fileName) {
    return fileUtil.getFile(fileName);
  }



  
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
