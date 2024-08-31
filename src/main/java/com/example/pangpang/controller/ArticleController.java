package com.example.pangpang.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.example.pangpang.dto.ArticleDTO;
import com.example.pangpang.dto.PageRequestDTO;
import com.example.pangpang.dto.PageResponseDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.ArticleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.*;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;


    // 게시글 작성
    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<Long> createArticle(@Valid @RequestBody ArticleDTO articleDTO, Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        Long articleId = articleService.createArticle(memberId, articleDTO);
        return ResponseEntity.ok(articleId);
    }



    // 전체 게시글 리스트
    @GetMapping("/list")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<PageResponseDTO<ArticleDTO>> list(
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

        PageResponseDTO<ArticleDTO> response = articleService.list(pageRequestDTO);
        return ResponseEntity.ok(response);
    }



    // 게시글 조회
    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable(name = "id") Long id,
            HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(articleService.getArticleById(id, request, response));
    }



    // 게시글 수정
    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<Map<String, String>> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO, Authentication auth){
        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        articleService.updateArticle(memberId, id, articleDTO);
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }



    // 게시글 삭제
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<Map<String, String>> deleteArticle(@PathVariable Long id, Authentication auth){
        Member member = (Member) auth.getPrincipal();
        Long memberId = member.getId();

        articleService.deleteArticle(memberId, id);
        return ResponseEntity.ok().body(Map.of("result", "success"));
    }



    // 로그인한 회원의 본인 게시글 목록 조회
    @GetMapping("/myArticles")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<PageResponseDTO<ArticleDTO>> getMyArticles(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Authentication auth) {

        Member member = (Member) auth.getPrincipal();
        Long memberId = member.getId();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .build();

            PageResponseDTO<ArticleDTO> response = articleService.listByMember(memberId, pageRequestDTO);
            return ResponseEntity.ok(response);
    }
}