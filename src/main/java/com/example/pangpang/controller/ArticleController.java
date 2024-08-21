package com.example.pangpang.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.example.pangpang.dto.ArticleDTO;
import com.example.pangpang.dto.PageRequestDTO;
import com.example.pangpang.dto.PageResponseDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.ArticleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;


    // 게시글 작성
    @PostMapping("/create")
    public ResponseEntity<Long> createArticle(@Valid @RequestBody ArticleDTO articleDTO, Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        Long articleId = articleService.createArticle(memberId, articleDTO);
        return ResponseEntity.ok(articleId);
    }


    // 전체 게시글 리스트
    @GetMapping("/list")
    public PageResponseDTO<ArticleDTO> list(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "12") int size,
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "searchBy", defaultValue = "title") String searchBy){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
        .page(page)
        .size(size)
        .search(search)
        .searchBy(searchBy)
        .build();

        return articleService.list(pageRequestDTO);
    }


    // 게시글 조회
    @GetMapping("/read/{id}")
    public ArticleDTO getArticleById(@PathVariable(name = "id") Long id){
        return articleService.getArticleById(id);
    }


    // 게시글 수정
    @PutMapping("/modify/{id}")
    public ResponseEntity<Void> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO, Authentication auth){
        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        articleService.updateArticle(memberId, id, articleDTO);
        return ResponseEntity.noContent().build();
    }


    // 게시글 삭제
    @DeleteMapping("/list/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}