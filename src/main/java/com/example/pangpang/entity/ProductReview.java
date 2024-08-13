package com.example.pangpang.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int rating;
  private String reviewContent;
  private String reviewFileName;

  @Builder.Default
  private LocalDateTime reviewData = LocalDateTime.now();
  
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @OneToOne
  private Member member;
}
