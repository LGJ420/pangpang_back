package com.example.pangpang.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.ArticleDTO;
import com.example.pangpang.dto.PageRequestDTO;
import com.example.pangpang.dto.PageResponseDTO;
import com.example.pangpang.entity.Article;
import com.example.pangpang.repository.ArticleRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ModelMapper modelMapper = new ModelMapper();
    private ArticleRepository articleRepository;

    public PageResponseDTO<ArticleDTO> list(PageRequestDTO pageRequestDTO){
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() -1 , pageRequestDTO.getSize(), Sort.by("id").descending());

        String search = pageRequestDTO.getSearch();

        Page<Article> result;

        if(search != null && !search.isEmpty()){
            result = articleRepository.findByArticleTitleContaining(search, pageable);
        }else{
            result = articleRepository.findAll(pageable);
        }

        List<ArticleDTO> dtoList = result.getContent().stream().map(article -> modelMapper.map(article, ArticleDTO.class)).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        PageResponseDTO<ArticleDTO> responseDTO = PageResponseDTO.<ArticleDTO>withAll().dtoList(dtoList).pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();

        return responseDTO;
    }

    public List<ArticleDTO> mainArticleList() {
        List<Article> result = articleRepository.findAll();

        List<ArticleDTO> dtoList = result.stream().map(article -> modelMapper.map(article, ArticleDTO.class)).collect(Collectors.toList());

        return dtoList;
    }

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

    @Transactional
    public ArticleDTO getArticleById(Long id){
        Optional<Article> result = articleRepository.findById(id);
        Article article = result.orElseThrow();
        ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
        return articleDTO;
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
