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
public class Orders {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderName;
    private String orderAddress;
    private String orderPhone;

    @OneToMany(mappedBy = "orders")
    private List<OrdersProduct> ordersProducts;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


}
