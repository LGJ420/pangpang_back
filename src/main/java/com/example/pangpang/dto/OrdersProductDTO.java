package com.example.pangpang.dto;

import java.util.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersProductDTO {
    
    private Long productId;

    @NotEmpty
    private String productTitle;

    @NotNull
    private String productContent;

    @Positive
    private int productPrice;

    @Positive
    private int cartCount;

    private boolean reviewExist;

    // 상품 이미지 파일 이름들을 나타내는 문자열 리스트
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();


}