package com.example.pangpang.entity;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String articleTitle;

    @Column(columnDefinition = "TEXT")
    private String articleContent;

    private String articleAuthor;

    @Column(name = "article_created", nullable = false, updatable = false)
    private LocalDateTime articleCreated;

    @Column(name = "article_updated")
    private LocalDateTime articleUpdated;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
