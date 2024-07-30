package com.example.pangpang.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[0-9]*$", message = "숫자(0~9)만 입력 가능합니다.")
    private String orderPhone;

    @Positive
    private Long memberId;

    @Positive
    private Long productId;
}
