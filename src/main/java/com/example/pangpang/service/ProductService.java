package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.*;
import com.example.pangpang.repository.*;
import com.example.pangpang.util.CustomFileUtil;

import jakarta.persistence.EntityNotFoundException;
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
  public Long addProduct(ProductDTO productDTO) {

    // ProductDTO를 Product 엔티티로 변환
    Product product = modelMapper.map(productDTO, Product.class);

    // 생성한 Product 객체를 ProductRepository를 사용하여 데이터베이스에 저장
    Product savedProduct = productRepository.save(product);

    // customFileUtil을 사용하여 상품에 첨부된 이미지 파일들을 저장
    List<String> fileNames = customFileUtil.saveFiles(productDTO.getFiles());

    // 각 이미지 파일 이름을 ProductImage 엔티티로 변환
    List<ProductImage> images = fileNames.stream()
        .map(fileName -> ProductImage.builder()
            .fileName(fileName)
            .product(savedProduct) // 저장한 상품과 연관
            .build())
        .collect(Collectors.toList());

    // 변환한 ProductImage 객체들을 ProductImageRepository를 통해 데이터베이스에 저장
    productImageRepository.saveAll(images);

    // 등록된 상품의 ID를 반환
    return savedProduct.getId();
  }


  /* 상품 수정하기 */
  public void modifyProduct(Long id, ProductDTO productDTO, List<MultipartFile> files) {

    Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

    product.setProductTitle(productDTO.getProductTitle());
    product.setProductContent(productDTO.getProductContent());
    product.setProductPrice(productDTO.getProductPrice());
    product.setProductDetailContent(productDTO.getProductDetailContent());
    product.setProductCategory(productDTO.getProductCategory());
    product.setProductStock(productDTO.getProductStock());

   // 기존 이미지 유지하고 새 이미지 추가
  if (files != null && !files.isEmpty()) {
    // 새 이미지 저장
    List<String> fileNames = customFileUtil.saveFiles(files);

    // 새 이미지 엔티티 생성
    List<ProductImage> images = fileNames.stream()
        .map(fileName -> ProductImage.builder()
            .fileName(fileName)
            .product(product) // 저장된 상품과 연관
            .build())
        .collect(Collectors.toList());

    // 새 이미지 DB에 저장
    productImageRepository.saveAll(images);
  }

  productRepository.save(product);
}



/* 상품 삭제하기 */
public void deleteProduct(Long id) {
  if(!productRepository.existsById(id)) {
    throw new RuntimeException("상품을 찾지 못했습니다." + id);
  }

  // 상품에 연결된 이미지 파일 삭제
  List<ProductImage> productImages = productImageRepository.findByProductId(id);
  List<String> fileNames = productImages.stream()
      .map(ProductImage::getFileName)
      .collect(Collectors.toList());

  // 이미지 파일 삭제
  customFileUtil.deleteFiles(fileNames);

  // 이미지 엔티티 삭제
  productImageRepository.deleteAll(productImages);

  // 상품 삭제
  productRepository.deleteById(id);
}


  /* 상품 목록 보기 */
  public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

    // 페이지 정의
    Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
        Sort.by("id").descending());

    // 검색 키워드 가져오기
    String search = pageRequestDTO.getSearch();
    String category = pageRequestDTO.getCategory();

    // 검색어가 존재하면 카테고리 필터를 무시하고 전체 검색
    if (search != null && !search.isEmpty()) {
      category = null; // 카테고리 필터를 리셋
    }

    // 첫 번째 단계: Product 목록만 조회
    Page<Product> productPage = productRepository.findProductsByCategoryAndSearch(category, search, pageable);

    // 현재 페이지의 Product ID 리스트 추출
    List<Long> productIds = productPage.getContent().stream()
        .map(Product::getId)
        .collect(Collectors.toList());

    // 두 번째 단계: Product ID 리스트로 관련된 이미지 조회
    List<ProductImage> allImages = productRepository.findImagesByProductIds(productIds);

    // 이미지 그룹화
    Map<Long, List<String>> productImagesMap = allImages.stream()
        .collect(Collectors.groupingBy(
            image -> image.getProduct().getId(),
            Collectors.mapping(ProductImage::getFileName, Collectors.toList())));

    // Product 목록을 ProductDTO로 변환하고 이미지 설정
    List<ProductDTO> dtoList = productPage.getContent().stream()
        .map(product -> {
          ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
          List<String> imageNames = productImagesMap.getOrDefault(product.getId(), Collections.emptyList());
          productDTO.setUploadFileNames(imageNames);
          return productDTO;
        })
        .collect(Collectors.toList());

    // 총 개수 가져오기
    long totalCount = productPage.getTotalElements();

    log.info("totalCount : " + totalCount);

    // PageResponseDTO 생성 및 반환
    return PageResponseDTO.<ProductDTO>withAll()
        .dtoList(dtoList)
        .pageRequestDTO(pageRequestDTO)
        .totalCount(totalCount)
        .build();
  }



  /* 메인 페이지 상품 목록 */
  public List<ProductDTO> mainList() {

    List<Product> products = productRepository.findAllRandomWithImages();

    List<ProductDTO> dtoList = products.stream()
        .map(product -> {
          ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

          // Product 객체에서 직접 이미지 리스트를 가져옴
          List<String> imageNames = product.getProductImage().stream()
              .map(ProductImage::getFileName)
              .collect(Collectors.toList());

          productDTO.setUploadFileNames(imageNames);

          return productDTO;
        })
        .collect(Collectors.toList());

    return dtoList;
  }

  /* 상품 상세보기 */
  public ProductDTO getDetail(Long id) {

    Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));
    ProductDTO dto = modelMapper.map(product, ProductDTO.class);

    List<String> imageNames = product.getProductImage().stream()
        .map(productImage -> productImage.getFileName())
        .collect(Collectors.toList());
    dto.setUploadFileNames(imageNames);

    return dto;
  }

}
