package com.example.pangpang.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.pangpang.entity.ProductImage;
import com.fasterxml.jackson.annotation.JsonFormat;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewDate;

    private Long memberId;
    private String memberImage;
    private String memberNickName;
    
    private Long productId;
    private String productTitle;
    private List<String> productImages;
}
