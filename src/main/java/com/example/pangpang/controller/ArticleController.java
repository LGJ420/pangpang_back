package com.example.pangpang.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.ArticleDTO;
import com.example.pangpang.dto.PageRequestDTO;
import com.example.pangpang.dto.PageResponseDTO;
import com.example.pangpang.service.ArticleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/create")
    public ResponseEntity<Long> createArticle(@Valid @RequestBody ArticleDTO articleDTO){
        Long articleId = articleService.createArticle(articleDTO);
        return ResponseEntity.ok(articleId);
    }

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

    @GetMapping("/read/{id}")
    public ArticleDTO getArticleById(@PathVariable(name = "id") Long id){
        // 조회수 증가
        articleService.incrementViewCount(id);
        
        return articleService.getArticleById(id);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<Void> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO){
        articleService.updateArticle(id, articleDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/list/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}