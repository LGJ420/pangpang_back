package com.example.pangpang.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    
    @NotEmpty(message = "내용은 반드시 작성해 주세요.")
    private String commentContent;
}
