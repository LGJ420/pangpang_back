package com.example.pangpang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.pangpang.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article>{

}
