package com.example.pangpang.controller;

import java.util.*;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.Member;
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
  @PostMapping("")
  public ResponseEntity<Long> addProduct(Authentication auth, ProductDTO productDTO) {

    Member member = (Member) auth.getPrincipal();
    Long memberId = member.getId();

    if (productDTO.getFiles() == null || productDTO.getFiles().isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }

    Long productId = productService.addProduct(memberId, productDTO);

    // 성공적인 응답 반환
    return ResponseEntity.status(HttpStatus.CREATED).body(productId);
  }

  /* 상품 수정 */
  @PutMapping("/{id}")
  public ResponseEntity<Void> modifyProduct(Authentication auth, @PathVariable(name = "id") Long id,
      @RequestParam Map<String, String> params,
      @RequestParam(value = "deleteImages", required = false) List<String> deleteImages,
      @RequestParam(value = "files", required = false) List<MultipartFile> files) {

    Member member = (Member) auth.getPrincipal();
    Long memberId = member.getId();

    // ProductDTO 파라미터 생성
    ProductDTO productDTO = new ProductDTO();
    productDTO.setProductTitle(params.get("productTitle"));
    productDTO.setProductContent(params.get("productContent"));
    productDTO.setProductPrice(Integer.parseInt(params.get("productPrice")));
    productDTO.setProductDetailContent(params.get("productDetailContent"));
    productDTO.setProductCategory(params.get("productCategory"));
    
    // 상품 수정
    productService.modifyProduct(memberId, id, productDTO, deleteImages, files);
    return ResponseEntity.noContent().build();
  }

  /* 상품 삭제 */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(Authentication auth, @PathVariable(name = "id") Long id) {

    Member member = (Member) auth.getPrincipal();
    Long memberId = member.getId();

    productService.deleteProduct(memberId, id);
    return ResponseEntity.noContent().build();
  }

  /* 이미지 조회 */
  @GetMapping("/view/{fileName}")
  public ResponseEntity<Resource> viewFileGET(@PathVariable(name = "fileName") String fileName) {
    return fileUtil.getFile(fileName);
  }

  /* 쇼핑페이지 - 상품 목록 보기 */
  @GetMapping("/list")
  public PageResponseDTO<ProductDTO> list(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "12") int size,
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "category", required = false) String category) {

    // URL에서 전달받은 데이터 PageRequestDTO에 저장
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
        .page(page)
        .size(size)
        .search(search)
        .category(category)
        .build();

    return productService.list(pageRequestDTO);
  }

  /* 상품 관리 페이지 - 상품 전체 목록 보기 */
  @GetMapping("/")
  public PageResponseDTO<ProductDTO> productList(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "12") int size,
      @RequestParam(value = "search", required = false) String search) {

    // URL에서 전달받은 데이터 PageRequestDTO에 저장
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
        .page(page)
        .size(size)
        .search(search)
        .build();

    return productService.productList(pageRequestDTO);
  }

  /* 상품 상세 보기 */
  @GetMapping("/{id}")
  public ProductDTO getDetail(@PathVariable(name = "id") Long id) {
    return productService.getDetail(id);
  }

  /* 상품 재고량만 수정 */
  @PutMapping("/stock/{id}")
  public ResponseEntity<Map<String, String>> modifyStock(
      Authentication auth,
      @PathVariable(name = "id") Long productId,
      @RequestBody ProductDTO productDTO) {

    Member member = (Member) auth.getPrincipal();
    Long memberId = member.getId();

    productService.modifyStock(memberId, productId, productDTO);

    return ResponseEntity.ok().body(Map.of("result", "success"));
  }
}
