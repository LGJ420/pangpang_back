package com.example.pangpang.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    
    @NotEmpty(message = "제목은 반드시 작성해 주세요.")
    @Size(max = 100, message = "100자 이상은 입력되지 않습니다.")
    private String articleTitle;

    @NotEmpty(message = "내용은 반드시 작성해 주세요.")
    private String articleContent;

    @NotEmpty(message = "작성자는 반드시 작성해주세요.")
    @Size(max = 20, message = "20자 이상은 입력되지 않습니다.")
    private String articleAuthor;
}
