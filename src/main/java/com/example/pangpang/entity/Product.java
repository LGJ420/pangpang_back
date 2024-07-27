package com.example.pangpang.entity;

import java.util.*;

import com.example.pangpang.entity.embed.ProductImage;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productTitle;
    private String productContent;
    private int productPrice;

    @OneToMany
    private List<ProductImage> productImages;

    @OneToMany
    private List<Orders> orders;
}
