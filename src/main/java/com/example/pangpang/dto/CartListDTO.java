package com.example.pangpang.dto;

import java.util.*;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartListDTO {
    
    private Long productId;
    private String productTitle;
    private String productContent;
    private int productPrice;
    private int cartCount;

    // 상품 이미지 파일 이름들을 나타내는 문자열 리스트
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();

}
