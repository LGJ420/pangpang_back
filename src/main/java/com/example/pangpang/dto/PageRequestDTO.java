package com.example.pangpang.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;


/*
 * 프론트에 주석보시면
 * 제가 page와 size가 페이지요청할때 꼭 필요하다고 적어놨습니다
 * PageRequestDTO로 그 요청을 하는겁니다
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;
}
