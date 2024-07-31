package com.example.pangpang.service;

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
import com.example.pangpang.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

  private final ModelMapper modelMapper;
  private final ProductRepository productRepository;

  /* 목록 보기 - 페이징 처리 */
  public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {

    // 테스트용
    // System.out.println("데이터 전달받고 있음 : " + pageRequestDTO.getSearch());
    // System.out.println("데이터 전달받고 있음 : " + pageRequestDTO.getPage());
    // System.out.println("데이터 전달받고 있음 : " + pageRequestDTO.getSize());
    

    // 페이지 정의
    Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
        Sort.by("id").descending());

    // 검색 키워드 가져오기
    String search = pageRequestDTO.getSearch();

    Page<Product> result;

    if (search != null && !search.isEmpty()) {
      result = productRepository.findByProductTitleContaining(search, pageable);
    } else {
      result = productRepository.findAll(pageable);
    }

    // Product 엔티티에서 페이징 정의한 것 기준으로 상품 목록 조회
    // Page<Product> result = productRepository.findAll(pageable);

    // Product 엔티티를 ProductDTO로 변환하여 리스트로 만듦
    List<ProductDTO> dtoList = result.getContent().stream().map(product -> modelMapper.map(product, ProductDTO.class))
        .collect(Collectors.toList());

    // 전체 상품의 개수 가져옴
    long totalCount = result.getTotalElements();

    // PageResponseDTO를 생성하여 dtoList, pageRequestDTO, totalCount를 포함하여 반환
    PageResponseDTO<ProductDTO> responseDTO = PageResponseDTO.<ProductDTO>withAll().dtoList(dtoList)
        .pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();

    return responseDTO;
  }

  /* 상품 상세보기 */
  public ProductDTO getDetail(Long id) {
    Optional<Product> result = productRepository.findById(id);
    Product product = result.orElseThrow();
    ProductDTO dto = modelMapper.map(product, ProductDTO.class);

    return dto;
  }
}
