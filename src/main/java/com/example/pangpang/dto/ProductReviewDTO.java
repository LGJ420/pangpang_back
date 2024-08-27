package com.example.pangpang.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

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

    private Long productId;
    private Long memberId;

    private String memberImage;
    private String memberNickName;
}
