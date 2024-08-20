package com.example.pangpang.dto;

import java.time.*;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {
    
    private Long id;
    
    private String noticeTitle;
    private String noticeContent;

    @Builder.Default
    private Long noticeHit = 0L;

    @Builder.Default
    private LocalDateTime noticeCreated = LocalDateTime.now();
    private LocalDateTime noticeUpdated;

    private Long memberId;
    private String memberNickname;

}
