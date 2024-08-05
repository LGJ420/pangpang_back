package com.example.pangpang.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.pangpang.dto.PageRequestDTO;
import com.example.pangpang.dto.PageResponseDTO;
import com.example.pangpang.dto.ProductDTO;
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

  @PostMapping("/")
  public Map<String, String> register(ProductDTO productDTO) {
    log.info("register : " + productDTO);
    List<MultipartFile> files = productDTO.getFiles();

    List<String> uploadFileNames = fileUtil.saveFiles(files);
    log.info(uploadFileNames);

    return Map.of("RESULT", "SUCCESS");
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
