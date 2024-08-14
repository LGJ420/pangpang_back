package com.example.pangpang.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.ProductReviewDTO;
import com.example.pangpang.entity.*;
import com.example.pangpang.repository.*;
import com.example.pangpang.util.CustomFileUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductReviewService {
    
    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CustomFileUtil customFileUtil;



    public void add(Long memberId, ProductReviewDTO productReviewDTO){

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("Member not found"));
            
        Product product = productRepository.findById(productReviewDTO.getProductId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        String fileName = customFileUtil.saveFile(productReviewDTO.getReviewFile());

        ProductReview productReview = ProductReview.builder()
            .rating(productReviewDTO.getRating())
            .reviewContent(productReviewDTO.getReviewContent())
            .reviewFileName(fileName)
            .product(product)
            .member(member)
            .build();

        productReviewRepository.save(productReview);
    }
}
