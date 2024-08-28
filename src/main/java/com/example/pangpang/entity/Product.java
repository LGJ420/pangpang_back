package com.example.pangpang.entity;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    private String productDetailContent; // 상품 긴 설명

    private int productStock; // 상품 재고량

    private int productTotalSales; // 상품 누적 판매량

    private int productUpdateSales; // 상품 재고량 수정 시 사용할 상품 판매량

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

    // 상품 재고량 수정
    public void changeProductStock(int productStock){

        if(productStock < 0 || productStock >= 101) {
            throw new IllegalArgumentException();
        }
        this.productStock = productStock;
        this.productUpdateSales = 0;
    }

    // 상품 수정
    public void changeProduct(String productTitle, String productContent, String productDetailContent, String productCategory , 
    int productPrice) {
        this.productTitle = productTitle;
        this.productContent = productContent;
        this.productDetailContent = productDetailContent;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
    }

}
