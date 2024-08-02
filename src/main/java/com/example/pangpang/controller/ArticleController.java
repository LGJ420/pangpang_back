package com.example.pangpang.controller;

import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.ArticleDTO;
import com.example.pangpang.entity.Article;
import com.example.pangpang.service.ArticleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/list")
    public ResponseEntity<Long> createArticle(@Valid @RequestBody ArticleDTO articleDTO){
        Long articleId = articleService.createArticle(articleDTO);
        return ResponseEntity.ok(articleId);
    }

    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllArticles(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "12") int size){
        Map<String,Object> response = articleService.getAllArticles(page,size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id){
        Article article = articleService.getArticleById(id);
        return ResponseEntity.ok(article);
    }

    @PutMapping("/list/{id}")
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
