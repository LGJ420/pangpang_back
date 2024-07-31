package com.example.pangpang.dto;

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

}
