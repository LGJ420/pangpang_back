package com.example.pangpang.entity;

import java.time.*;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String noticeTitle;

    @Column(length = 1000)
    private String noticeContent;

    @Builder.Default
    private Long noticeHit = 0L;

    @Builder.Default
    private LocalDateTime noticeCreated = LocalDateTime.now();

    private LocalDateTime noticeUpdated;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE)
    @OrderBy("commentCreated asc") // 댓글을 작성 시간 기준으로 정렬
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    
    public void changeNoticeTitle(String title) {

        this.noticeTitle = title;
    }
    
    public void changeNoticeContent(String content) {

        this.noticeContent = content;
    }

    public void changeNoticeUpdated(LocalDateTime date) {

        this.noticeUpdated = date;
    }

}
