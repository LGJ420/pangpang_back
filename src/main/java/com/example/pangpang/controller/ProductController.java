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
  public ResponseEntity<Long> addProduct(ProductDTO productDTO) {

    if (productDTO.getFiles() == null || productDTO.getFiles().isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }

    Long productId = productService.addProduct(productDTO);

    // 성공적인 응답 반환
    return ResponseEntity.status(HttpStatus.CREATED).body(productId);
  }

  
  /* 상품 수정 */
  @PutMapping("/modify/{id}")
  public ResponseEntity<Void> modifyProduct(@PathVariable(name = "id") Long id, 
      @RequestParam Map<String, String> params,
      @RequestParam(value = "files", required = false) List<MultipartFile> files) {

    // ProductDTO 파라미터 생성
    ProductDTO productDTO = new ProductDTO();
    productDTO.setProductTitle(params.get("productTitle"));
    productDTO.setProductContent(params.get("productContent"));
    productDTO.setProductPrice(Integer.parseInt(params.get("productPrice")));
    productDTO.setProductDetailContent(params.get("productDetailContent"));
    productDTO.setProductCategory(params.get("productCategory"));

    // 상품 수정
    productService.modifyProduct(id, productDTO, files);
    return ResponseEntity.noContent().build();
  }


  /* 상품 삭제 */
  @DeleteMapping("read/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
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
