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
    @OneToMany(mappedBy = "product")
    private List<Cart> carts;

    @OneToMany(mappedBy = "product")
    private List<OrdersProduct> ordersProducts;

    // 상품 이미지
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImage;


    // // 가격 수정
    // public void changePrice(int price) {
    // this.productPrice = price;
    // }


    // // 내용 수정
    // public void changeContent(String content) {
    // this.productContent = content;
    // }


    // // 제목 수정
    // public void changeTitle(String title) {
    // this.productTitle = title;
    // }


    

}
