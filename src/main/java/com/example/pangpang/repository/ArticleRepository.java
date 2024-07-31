package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pangpang.entity.Article;
import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long>{
    Article findByArticleTitle(String articleTitle);
    Article findByArticleTitleAndArticleContent(String ArticleTitle,String articleContent);
    List<Article> findByArticleLike(String articleTitle);
}
