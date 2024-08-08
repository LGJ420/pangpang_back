package com.example.pangpang.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.pangpang.dto.PageRequestDTO;
import com.example.pangpang.dto.PageResponseDTO;
import com.example.pangpang.dto.ProductDTO;
import com.example.pangpang.entity.Product;
import com.example.pangpang.entity.ProductImage;
import com.example.pangpang.repository.ProductImageRepository;
import com.example.pangpang.repository.ProductRepository;
import com.example.pangpang.util.CustomFileUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ProductService {

  private final ModelMapper modelMapper;
  private final ProductRepository productRepository;
  private final CustomFileUtil customFileUtil;
  private final ProductImageRepository productImageRepository;

  /* 상품 등록 */
  @Transactional
  public Long addProduct(ProductDTO productDTO) {
    // 상품 생성
    Product product = Product.builder()
        .productTitle(productDTO.getProductTitle())
        .productContent(productDTO.getProductContent())
        .productPrice(productDTO.getProductPrice())
        .build();

    // 상품 저장 및 새 변수 할당
    Product savedProduct = productRepository.save(product);

    // 이미지 파일 저장 및 파일 이름 리스트 받기
    List<String> fileNames = customFileUtil.saveFiles(productDTO.getFiles());

    // 저장된 파일 이름 리스트를 ProductImage 객체로 변환
    List<ProductImage> images = fileNames.stream()
        .map(fileName -> ProductImage.builder()
            .fileName(fileName)
            .product(savedProduct) // 저장한 상품과 연관
            .build())
        .collect(Collectors.toList());

    // 이미지 저장 (ProductImage를 별도로 저장)
    productImageRepository.saveAll(images);

    return savedProduct.getId();
  }



  /* 목록 보기 - 페이징 처리 */
  public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

    // 페이지 정의
    Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
        Sort.by("id").descending());

    // 검색 키워드 가져오기
    String search = pageRequestDTO.getSearch();

    // Product 엔티티 정의
    Page<Object[]> result;

    // 검색 키워드가 비워져 있지 않으면 검색 키워드 기준으로 페이징, 아니면 상품 목록 전체 출력
    if (search != null && !search.isEmpty()) {
      result = productRepository.findByProductTitleContainingWithImage(search, pageable);
    } else {
      // result = productRepository.findAll(pageable);
      result = productRepository.selectList(pageable);
    }

    // Product 엔티티를 ProductDTO로 변환하여 리스트로 만듦
    // Product 엔티티를 ProductDTO로 변환하여 리스트로 만듦
    List<ProductDTO> dtoList = result.getContent().stream()
        .map(arr -> {
          Product product = (Product) arr[0];
          ProductImage productImage = (ProductImage) arr[1];

          // ProductDTO를 빌더 패턴으로 생성합니다.
          ProductDTO productDTO = ProductDTO.builder()
              .id(product.getId())
              .productTitle(product.getProductTitle())
              .productContent(product.getProductContent())
              .productPrice(product.getProductPrice())
              .build();

          // 이미지 파일 이름이 있을 경우, 리스트에 추가합니다.
          if (productImage != null) {
            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));
          } else {
            productDTO.setUploadFileNames(Collections.emptyList()); // 이미지가 없을 경우 빈 리스트로 설정
          }

          return productDTO;
        })
        .collect(Collectors.toList());

    // 전체 상품의 개수 가져옴
    long totalCount = result.getTotalElements();

    // PageResponseDTO를 생성하여 dtoList, pageRequestDTO, totalCount를 포함하여 반환
    PageResponseDTO<ProductDTO> responseDTO = PageResponseDTO.<ProductDTO>withAll()
        .dtoList(dtoList)
        .pageRequestDTO(pageRequestDTO)
        .totalCount(totalCount)
        .build();

    return responseDTO;
  }



  /* 메인 페이지 상품 목록 */
  public PageResponseDTO<ProductDTO> mainList(PageRequestDTO pageRequestDTO) {

    Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
        Sort.by("id").descending());

    Page<Product> result = productRepository.findAllRandom(pageable);

    List<ProductDTO> dtoList = result.getContent().stream()
        .map(product -> modelMapper.map(product, ProductDTO.class))
        .collect(Collectors.toList());

    long totalCount = result.getTotalElements();

    PageResponseDTO<ProductDTO> responseDTO = PageResponseDTO.<ProductDTO>withAll().dtoList(dtoList)
        .pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();

    return responseDTO;
  }



  /* 상품 상세보기 */
  public ProductDTO getDetail(Long id) {
    Optional<Product> result = productRepository.findById(id);
    Product product = result.orElseThrow();
    ProductDTO dto = modelMapper.map(product, ProductDTO.class);

    // 상품의 이미지 URL 목록 설정
    // List<String> imageUrls = product.getProductImage().stream()
    // .map(img -> "/api/product/view/" + img.getFileName())
    // .collect(Collectors.toList());

    // dto.setImageUrls(imageUrls);

    return dto;
  }

}
