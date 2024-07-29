package com.example.pangpang.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDTO {
    
    @Positive
    private int orderCount;

    @NotEmpty
    private String orderAddress;

    @NotEmpty
    private String orderPhone;

    @Positive
    private Long memberId;

    @Positive
    private Long productId;
}
