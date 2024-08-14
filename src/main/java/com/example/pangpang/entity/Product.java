package com.example.pangpang.entity;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productTitle; // 상품 이름
    private String productContent; // 상품 설명
    private int productPrice; // 상품 가격

    // 상품 주문
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Cart> carts;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrdersProduct> ordersProducts;

    // 상품 이미지
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImage;

    // 상품 리뷰
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductReview> productReviews;

}
