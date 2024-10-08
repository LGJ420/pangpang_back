package com.example.pangpang.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    
    @Positive
    private int cartCount;
    
    @Positive
    private Long productId;

}
