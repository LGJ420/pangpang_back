package com.example.pangpang.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Data;


/*
 * PageResponseDTO가 감이 안잡힐수 있으니 설명을 써놓았습니다
 * Page는 결국 목록보기에 써야되는겁니다
 * 
 * 우리는 사용자와 반드시 Entity가 아닌 DTO로만 통신해야됩니다
 * 그럼 ArticleDTO를 리턴하면?
 * 글 하나 리턴되겠죠?
 * 
 * PageResponseDTO<ArticleDTO>를 리턴하면?
 * 글을 페이지 목록형태로 보내주는 겁니다
 */
@Data
public class PageResponseDTO<E> {
    
    private List<E> dtoList; // 여기에 DTO들이 가득 들어있습니다
    private List<Integer> pageNumList; // 이게 1,2,3,4,5~~~ 페이지들 번호리스트입니다
    private PageRequestDTO pageRequestDTO; // 요청을 어떻게 했는지 기억해 놓는겁니다, 이래야 뭐 그때로 다시 돌아갈수 있대나 뭐래나
    private boolean prev, next; // 이전버튼 다음버튼 입니다, boolean인 이유, 글이 3개밖에없으면 다음버튼이 없겠죠? 그럴땐 false 글이 수백개 있으면 true가 되는겁니다
    private int totalCount, prevPage, nextPage, totalPage, current; // 이거 변수 어따쓰는지 저도 까먹었습니다



    /*
     * 주석읽다가 프로젝트 세월 다갈수 있습니다
     * 그냥 원리는 모르겠지만
     * 아아~ 이거쓰면 DTO를 페이지화 시켜서 받을수 있겠구나~ 하고 넘어가주세요
     */
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {

        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int)totalCount;

        int end = (int)(Math.ceil(pageRequestDTO.getPage()/10.0))*10;

        int start = end - 9;

        int last = (int)(Math.ceil((totalCount/(double)pageRequestDTO.getSize())));

        end = end > last ? last : end;

        this.prev = start > 1;


        this.next = totalCount > end * pageRequestDTO.getSize();

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        if(prev) {
            this.prevPage = start - 1;
        }

        if(next) {
            this.nextPage = end + 1;
        }

        this.totalPage = this.pageNumList.size();

        this.current = pageRequestDTO.getPage();
    }
}
