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
import com.example.pangpang.entity.Member;
import com.example.pangpang.repository.ArticleRepository;
import com.example.pangpang.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public PageResponseDTO<ArticleDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("id").descending());

        String search = pageRequestDTO.getSearch();
        String searchBy = pageRequestDTO.getSearchBy();

        Page<Article> result;
        if("title".equalsIgnoreCase(searchBy)){
            result = articleRepository.findByArticleTitleContaining(search, pageable);
        }else if ("author".equalsIgnoreCase(searchBy)) {
            result = articleRepository.findByMemberMemberNicknameContaining(search, pageable);
        } else {
            result = articleRepository.findAll(pageable);
        }

        List<ArticleDTO> dtoList = result.getContent().stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class)).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        PageResponseDTO<ArticleDTO> responseDTO = PageResponseDTO.<ArticleDTO>withAll().dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();

        return responseDTO;
    }

    public List<ArticleDTO> mainArticleList() {
        List<Article> result = articleRepository.findAll();

        List<ArticleDTO> dtoList = result.stream().map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());

        return dtoList;
    }

    @Transactional
    public Long createArticle(Long memberId, ArticleDTO articleDTO) {

        Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        

        Article article = Article.builder()
                .articleTitle(articleDTO.getArticleTitle())
                .articleContent(articleDTO.getArticleContent())
                .articleCreated(LocalDateTime.now())
                .member(member)
                // .viewCount(0L) // 조회수 초기화
                .build();
        article = articleRepository.save(article);
        return article.getId();
    }

    @Transactional
    public ArticleDTO getArticleById(Long id) {
        // 게시글 조회
        Optional<Article> result = articleRepository.findById(id);
        Article article = result.orElseThrow();

        ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);

        articleDTO.setMemberId(article.getMember().getId());

        return articleDTO;
    }

    @Transactional
    public void updateArticle(Long memberId, Long id, ArticleDTO articleDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("글을 찾지 못했습니다." + id));

        // 로그인한 회원이 다를 시에는 다른 회원의 글을 수정할 권한을 주지 않음
        if (!article.getMember().getId().equals(memberId)){
            throw new RuntimeException("이 글을 수정할 권한이 없습니다.");
        }

        article.setArticleTitle(articleDTO.getArticleTitle());
        article.setArticleContent(articleDTO.getArticleContent());
        article.setArticleUpdated(LocalDateTime.now());
        article.setMember(member);
        articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new RuntimeException("글을 찾지 못했습니다. " + id);
        }
        articleRepository.deleteById(id);
    }

    // @Transactional
    // public void incrementViewCount(Long id) {
    //     articleRepository.incrementViewCount(id);
    // }
}