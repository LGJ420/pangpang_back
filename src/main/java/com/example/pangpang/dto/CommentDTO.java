package com.example.pangpang.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    
    private Long id; // Comment 자체의 ID

    private Long articleId; // 관련된 Article의 ID

    @NotEmpty(message = "내용은 반드시 작성해 주세요.")
    @Size(max = 3000, message = "3000자 이상은 입력되지 않습니다.")
    private String commentContent;
    
    private String memberNickname;
    
    private Long memberId;
    
    private LocalDateTime commentCreated;
    private LocalDateTime commentUpdated;

    private String articleTitle;
}
