package com.example.pangpang.entity;

import java.time.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentContent;
    private LocalDateTime commentCreated;
    private LocalDateTime commentUpdated;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Article article;
}
