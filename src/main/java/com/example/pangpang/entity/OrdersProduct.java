package com.example.pangpang.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrdersProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int count;

    @OneToOne
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;
}
