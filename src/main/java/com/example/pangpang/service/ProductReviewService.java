package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
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

    public void add(Long memberId, ProductReviewDTO productReviewDTO) {

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

    public List<ProductReviewDTO> list(Long productId) {

        Sort sort = Sort.by("reviewDate").descending();
        List<ProductReview> productReviews = productReviewRepository.findByProductId(productId, sort);

        List<ProductReviewDTO> productReviewDTOs = productReviews.stream()
            .map(review->{
                ProductReviewDTO productReviewDTO = ProductReviewDTO.builder()
                    .rating(review.getRating())
                    .reviewContent(review.getReviewContent())
                    .reviewFileName(review.getReviewFileName())
                    .reviewDate(review.getReviewDate())
                    .productId(review.getProduct().getId())
                    .memberId(review.getMember().getId())
                    .memberImage(review.getMember().getMemberImage())
                    .memberNickName(review.getMember().getMemberNickname())
                    .build();
                
                return productReviewDTO;
            })
            .collect(Collectors.toList());
        
        return productReviewDTOs;
    }

    public List<ProductReviewDTO> mylist(Long id) {

        Sort sort = Sort.by("reviewDate").descending();
        List<ProductReview> productReviews = productReviewRepository.findByMemberId(id, sort);

        List<ProductReviewDTO> productReviewDTOs = productReviews.stream()
            .map(review->{

                List<String> productImageFilenames = review.getProduct().getProductImage().stream()
                    .map(ProductImage::getFileName)
                    .collect(Collectors.toList());

                ProductReviewDTO productReviewDTO = ProductReviewDTO.builder()
                    .rating(review.getRating())
                    .reviewContent(review.getReviewContent())
                    .reviewFileName(review.getReviewFileName())
                    .reviewDate(review.getReviewDate())
                    .memberId(review.getMember().getId())
                    .memberImage(review.getMember().getMemberImage())
                    .memberNickName(review.getMember().getMemberNickname())
                    .productId(review.getProduct().getId())
                    .productTitle(review.getProduct().getProductTitle())
                    .productImages(productImageFilenames)
                    .build();
                
                return productReviewDTO;
            })
            .collect(Collectors.toList());

        return productReviewDTOs;
    }
}
