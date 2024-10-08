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

    @Column(name = "article_created", nullable = false, updatable = false)
    private LocalDateTime articleCreated;

    @Column(name = "article_updated")
    private LocalDateTime articleUpdated;

    @Builder.Default
    @Column(name = "view_count")
    private Long viewCount = 0L;  // 조회수 필드 추가

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    @OrderBy("commentCreated asc") // 댓글을 작성 시간 기준으로 정렬
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;
}
