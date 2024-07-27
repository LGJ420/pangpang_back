package com.example.pangpang.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    
    @NotEmpty
    private String commentContent;
}
