package com.example.pangpang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.ArticleDTO;
import com.example.pangpang.entity.Article;
import com.example.pangpang.service.ArticleService;

import jakarta.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/api/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseEntity<Article> createArticle(@Valid @RequestBody ArticleDTO articleDTO){
        Article article = Article.builder()
        .articleTitle(articleDTO.getArticleTitle())
        .articleContent(articleDTO.getArticleContent())
        .articleAuthor(articleDTO.getArticleAuthor())
        .build();
    Article createdArticle = articleService.createArticle(article);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);    
    }
    
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.getArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id){
        Optional<Article> article = articleService.getArticleById(id);
        return article.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDTO articleDTO){
        Article articleDetails = Article.builder()
        .articleTitle(articleDTO.getArticleTitle())
        .articleContent(articleDTO.getArticleContent())
        .articleAuthor(articleDTO.getArticleAuthor())
        .build();
        Optional<Article> updatedArticle = articleService.updateArticle(id, articleDetails);
        return updatedArticle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        boolean isDeleted = articleService.deleteArticle(id);
        if(isDeleted){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
