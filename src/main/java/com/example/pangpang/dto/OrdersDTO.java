package com.example.pangpang.dto;

import java.util.*;
import java.time.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDTO {
    
    @NotEmpty(message = "배송지 이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "배송지 주소는 필수입니다.")
    private String address;

    @NotEmpty
    @Pattern(regexp = "^[0-9]*$", message = "숫자(0~9)만 입력 가능합니다.")
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    @JsonProperty("dtoList")
    private List<OrdersProductDTO> ordersProducts;

    private Long memberId;
}
