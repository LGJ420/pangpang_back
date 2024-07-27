package com.example.pangpang.entity;

import java.time.*;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String articleTitle;
    private String articleContent;
    private LocalDateTime articleCreated;
    private LocalDateTime articleUpdated;

    @ManyToOne
    private Member member;

    @OneToMany
    private List<Comment> comments;
}
