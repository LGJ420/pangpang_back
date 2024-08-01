package com.example.pangpang.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersProductDTO {
    
    private Long id;

    @NotEmpty
    private String productTitle;

    @NotNull
    private String productContent;

    @Positive
    private int productPrice;

    @Positive
    private int cartCount;
}