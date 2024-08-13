package com.example.pangpang.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewDTO {
    
    private int rating;
    private String reviewContent;
    private String reviewFileName;
}
