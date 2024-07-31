package com.example.pangpang.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pangpang.entity.Article;
import com.example.pangpang.repository.ArticleRepository;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public Article createArticle(Article article){
        return articleRepository.save(article);
    }

    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Long id){
        return articleRepository.findById(id);
    }
}
