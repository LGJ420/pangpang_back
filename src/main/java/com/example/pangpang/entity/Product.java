package com.example.pangpang.entity;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String productTitle; // 상품 이름
    @NotEmpty
    private String productContent; // 상품 설명
    private int productPrice; // 상품 가격
    @NotEmpty
    private String productCategory; // 상품 카테고리

    @NotEmpty
    @Column(length = 1000)
    private String productDetailContent;  // 상품 긴 설명

    private LocalDateTime productCreated;

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


    // Builder 패턴 사용 안하므로 여기서 엔티티의 기본값 설정
    @PrePersist
    public void prePersist() {
        if (this.productCreated == null) {
            this.productCreated = LocalDateTime.now();
        }
    }


}
