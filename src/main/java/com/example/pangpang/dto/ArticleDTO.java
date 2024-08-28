package com.example.pangpang.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    private Long id;

    @NotEmpty(message = "제목은 반드시 작성해 주세요.")
    @Size(max = 100, message = "100자 이상은 입력되지 않습니다.")
    private String articleTitle;

    @NotEmpty(message = "내용은 반드시 작성해 주세요.")
    private String articleContent;

    private String memberNickname;

    private Long memberId;

    private String memberRole;

    private LocalDateTime articleCreated; 
    private LocalDateTime articleUpdated;

    private Long viewCount;

    private Long commentCount;
}
