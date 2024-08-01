package com.example.pangpang.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.ArticleDTO;
import com.example.pangpang.entity.Article;
import com.example.pangpang.repository.ArticleRepository;

import java.util.*;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Transactional
    public Long createArticle(ArticleDTO articleDTO){
        Article article = Article.builder()
        .articleTitle(articleDTO.getArticleTitle())
        .articleContent(articleDTO.getArticleContent())
        .articleAuthor(articleDTO.getArticleAuthor())
        .articleCreated(LocalDateTime.now())
        .build();
        article = articleRepository.save(article);
        return article.getId();
    }

    @Transactional(readOnly = true)
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Article getArticleById(Long id){
        return articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("글을 찾지 못했습니다.:" + id));
    }

    @Transactional
    public void updateArticle(Long id, ArticleDTO articleDTO){
        Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("글을 찾지 못했습니다." + id));

        article.setArticleTitle(articleDTO.getArticleTitle());
        article.setArticleContent(articleDTO.getArticleContent());
        article.setArticleAuthor(articleDTO.getArticleAuthor());
        article.setArticleUpdated(LocalDateTime.now());
        articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(Long id){
        if (!articleRepository.existsById(id)){
           throw new RuntimeException("글을 찾지 못했습니다. " + id);
        }
        articleRepository.deleteById(id);
    }
}
