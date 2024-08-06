package com.example.pangpang.entity;

import java.util.*;

// import com.example.pangpang.entity.embed.ProductImage;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString(exclude = "imageList")
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

    

    // @ElementCollection
    // @Builder.Default
    // private List<ProductImage> imageList = new ArrayList<>();

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

    // // 이미지 등록
    // public void addImage(ProductImage image) {
    // image.setNum((long)this.imageList.size());
    // imageList.add(image);
    // }

    // // 이미지 파일명 등록
    // public void addImageString (String fileName) {
    // ProductImage productImage =
    // ProductImage.builder().fileName(fileName).build();
    // addImage(productImage);
    // }

    // // imageList의 모든 이미지 제거
    // public void clearList() {
    // this.imageList.clear();
    // }

}
