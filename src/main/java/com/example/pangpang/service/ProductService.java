package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.*;
import com.example.pangpang.repository.*;
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
  public void modifyProduct(Long id) {
    
  }


  

  /* 상품 목록 보기 */
  public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

    // 페이지 정의
    Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
        Sort.by("id").descending());

    // 검색 키워드 가져오기
    String search = pageRequestDTO.getSearch();

    // 상품 목록 페이지
    Page<Product> productPage;

    // 검색어 있으면 if문 실행, 아니면 else문 실행
    if (search != null && !search.isEmpty()) {
      productPage = productRepository.findByProductTitleContainingWithImage(search, pageable);
    } else {
      productPage = productRepository.findAll(pageable);
    }

    // 현제 페이지의 상품 목록에서 상품 id 추출해 리스트로 만듦
    List<Long> productIds = productPage.getContent().stream()
        .map(product -> product.getId())
        .collect(Collectors.toList());

    // 상품 이미지 조회
    List<ProductImage> allImages = productImageRepository.findByProductIdIn(productIds);

    // 이미지 그룹화 - 각 상품 id에 대해 이미지 파일 이름을 리스트로 매핑
    // productImagesMap의 키는 상품 id, 값은 이미지 파일 이름 리스트
    Map<Long, List<String>> productImagesMap = allImages.stream()
        .collect(Collectors.groupingBy(
            image -> image.getProduct().getId(),
            Collectors.mapping(productImage -> productImage.getFileName(), Collectors.toList())));

    // 상품 목록을 productDTO로 변환
    List<ProductDTO> dtoList = productPage.getContent().stream()
        .map(product -> {
          ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
          // 상품 id에 관련된 이미지 리스트 가져오고, 없는 경우 빈 리스트로 설정
          List<String> imageNames = productImagesMap.getOrDefault(product.getId(), Collections.emptyList());
          productDTO.setUploadFileNames(imageNames);
          return productDTO;
        })
        .collect(Collectors.toList());

    long totalCount = productPage.getTotalElements();

    PageResponseDTO<ProductDTO> responseDTO = PageResponseDTO.<ProductDTO>withAll()
        .dtoList(dtoList)
        .pageRequestDTO(pageRequestDTO)
        .totalCount(totalCount)
        .build();

    return responseDTO;
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
