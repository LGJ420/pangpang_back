package com.example.pangpang.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  private String fileName;  // 이미지 파일명


  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;



}
