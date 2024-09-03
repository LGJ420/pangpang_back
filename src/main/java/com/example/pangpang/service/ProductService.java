package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.*;
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
  private final MemberRepository memberRepository;

  /* 상품 등록 */
  public Long addProduct(Long memberId, ProductDTO productDTO) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("Member not found"));

    if (!member.getMemberRole().equals("Admin")) {

      throw new RuntimeException("운영자가 아니면 상품 등록이 불가능합니다.");
    }

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
  public void modifyProduct(Long memberId, Long id, ProductDTO productDTO, List<String> deleteImages,
      List<MultipartFile> files) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("Member not found"));

    if (!member.getMemberRole().equals("Admin")) {

      throw new RuntimeException("운영자가 아니면 상품 수정이 불가능합니다.");
    }

    Product product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Product not found"));

    product.changeProduct(productDTO.getProductTitle(), productDTO.getProductContent(),
        productDTO.getProductDetailContent(), productDTO.getProductCategory(), productDTO.getProductPrice());

    // 기존 이미지 삭제
    if (deleteImages != null && !deleteImages.isEmpty()) {
      // 이미지 삭제 로직
      productImageRepository.deleteByFileNameIn(deleteImages);
      customFileUtil.deleteFiles(deleteImages); // 파일 시스템에서 삭제
    }

    // 새 이미지 저장
    if (files != null && !files.isEmpty()) {
      List<String> fileNames = customFileUtil.saveFiles(files);

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
  public void deleteProduct(Long memberId, Long id) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("Member not found"));

    if (!member.getMemberRole().equals("Admin")) {

      throw new RuntimeException("운영자가 아니면 상품 삭제가 불가능합니다.");
    }

    if (!productRepository.existsById(id)) {
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

  /* 쇼핑 페이지 - 상품 목록 보기 */
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
          productDTO.setProductTotalSales(product.getProductTotalSales());
          productDTO.setProductStock(product.getProductStock() - product.getProductUpdateSales());
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

  /* 상품 관리 페이지 - 상품 전체 목록 보기 (재고량 0인 상품도 포함) */
  public PageResponseDTO<ProductDTO> productList(PageRequestDTO pageRequestDTO) {

    // 페이지 정의
    Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
        Sort.by("id").descending());

    // 검색 키워드 가져오기
    String search = pageRequestDTO.getSearch();

    // 첫 번째 단계: Product 목록만 조회
    Page<Product> productPage = productRepository.findByProductTitleContainingWithImage(search, pageable);

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
          productDTO.setProductTotalSales(product.getProductTotalSales());
          productDTO.setProductStock(product.getProductStock() - product.getProductUpdateSales());
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

    // 1. 랜덤으로 상품 리스트 가져오기
    List<Product> products = productRepository.findAllRandom();

    // 2. 상품 ID 리스트 추출
    List<Long> productIds = products.stream()
        .map(product -> product.getId())
        .collect(Collectors.toList());

    // 3. 해당 상품들의 이미지 리스트 가져오기
    List<ProductImage> allImages = productRepository.findImagesByProductIds(productIds);

    // 4. 이미지 그룹화
    Map<Long, List<String>> productImagesMap = allImages.stream()
        .collect(Collectors.groupingBy(
            image -> image.getProduct().getId(), // 아이디별로 그룹화 (키 값 = 상품 아이디)
            Collectors.mapping(productImage -> productImage.getFileName(), Collectors.toList()))); // value 값은 이미지 리스트

    // 5. ProductDTO로 변환하고 이미지 설정
    List<ProductDTO> dtoList = products.stream()
        .map(product -> {
          ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
          List<String> imageNames = productImagesMap.getOrDefault(product.getId(), Collections.emptyList());
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

  /* 상품 재고량만 수정 */
  public void modifyStock(Long memberId, Long productId, ProductDTO productDTO) {

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("Member not found"));

    if (!member.getMemberRole().equals("Admin")) {

      throw new RuntimeException("운영자가 아니면 재고량 수정이 불가능합니다.");
    }

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new EntityNotFoundException("Product not found"));

    product.changeProductStock(productDTO.getProductStock());

    productRepository.save(product);
  }

}
