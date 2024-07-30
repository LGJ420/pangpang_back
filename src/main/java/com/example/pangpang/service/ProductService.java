package com.example.pangpang.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

      Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

      Page<Product> result = productRepository.findAll(pageable);

      List<ProductDTO> dtoList = result.getContent().stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

      long totalCount = result.getTotalElements();

      PageResponseDTO<ProductDTO> responseDTO = PageResponseDTO.<ProductDTO>withAll().dtoList(dtoList).pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();

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
