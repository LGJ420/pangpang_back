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

    @ElementCollection
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "product")
    private List<Orders> orders;
}
