package com.example.pangpang.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    
    @NotEmpty
    private String articleTitle;

    @NotNull
    private String articleContent;
}
