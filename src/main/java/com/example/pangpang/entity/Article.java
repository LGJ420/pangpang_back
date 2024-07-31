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

    private LocalDateTime articleCreated;
    private LocalDateTime articleUpdated;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

  //  @ManyToOne
  //  private SiteUser author;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

//  @ManyToMany
//  Set<SiteUser> voter
}
