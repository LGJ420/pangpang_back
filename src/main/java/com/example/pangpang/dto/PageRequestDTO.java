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

    /* 검색 키워드 - 상품 목록 검색에서 사용 */
    private String search;

    /* 검색 기준 - 제목(title) 또는 작성자(author) */
    private String searchBy;

    private String category;
}
