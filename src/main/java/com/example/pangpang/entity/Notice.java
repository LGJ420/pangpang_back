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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
