package com.example.pangpang.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewDTO {
    
    private int rating;
    private String reviewContent;

    private MultipartFile reviewFile;
    private String reviewFileName;

    private Long productId;
}
