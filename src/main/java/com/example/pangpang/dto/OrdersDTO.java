package com.example.pangpang.dto;

import java.util.*;

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
    private String name;

    @NotEmpty
    private String adress;

    @NotEmpty
    @Pattern(regexp = "^[0-9]*$", message = "숫자(0~9)만 입력 가능합니다.")
    private String phone;

    @Positive
    private List<OrdersProductDTO> products;
}
