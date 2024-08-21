package com.example.pangpang.entity;

import java.time.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false, length = 3000)
    private String commentContent;

    @Column(name = "comment_created", updatable = false, nullable = false)
    private LocalDateTime commentCreated;

    @Column(name = "comment_updated")
    private LocalDateTime commentUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
