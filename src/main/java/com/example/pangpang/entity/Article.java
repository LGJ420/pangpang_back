package com.example.pangpang.entity;

import jakarta.persistence.*;

@Entity
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
