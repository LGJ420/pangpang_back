package com.example.pangpang.dto;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDTO {
    
    @Positive(message = "배송지 이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "배송지 주소는 필수입니다.")
    private String address;

    @NotEmpty
    @Pattern(regexp = "^[0-9]*$", message = "숫자(0~9)만 입력 가능합니다.")
    private String phone;

    @JsonProperty("dtoList")
    private List<OrdersProductDTO> ordersProducts;

    @Positive
    private Long memberId;
}
