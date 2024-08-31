package com.example.pangpang.service;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
import com.example.pangpang.repository.CommentRepository;
import com.example.pangpang.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    


    // 게시글 페이지네이션 및 검색
    public PageResponseDTO<ArticleDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("id").descending());

        String search = pageRequestDTO.getSearch();
        String searchBy = pageRequestDTO.getSearchBy();

        // 검색 조건(글 제목 or 회원 닉네임)
        Page<Article> result;
        if ("title".equalsIgnoreCase(searchBy)) {
            result = articleRepository.findByArticleTitleContaining(search, pageable);
        } else if ("author".equalsIgnoreCase(searchBy)) {
            result = articleRepository.findByMemberMemberNicknameContaining(search, pageable);
        } else {
            result = articleRepository.findAll(pageable);
        }

        List<ArticleDTO> dtoList = result.getContent().stream()
                .map(article -> {
                    ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
                    Long commentCount = commentRepository.countByArticle(article);
                    articleDTO.setCommentCount(commentCount);
                    return articleDTO;
                })
                .collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        PageResponseDTO<ArticleDTO> responseDTO = PageResponseDTO.<ArticleDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(totalCount)
                .build();

        return responseDTO;
    }

    public List<ArticleDTO> mainArticleList() {
        List<Article> result = articleRepository.findAll();

        List<ArticleDTO> dtoList = result.stream()
                .map(article -> {
                    ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
                    Long commentCount = commentRepository.countByArticle(article);
                    articleDTO.setCommentCount(commentCount);
                    return articleDTO;
                })
                .collect(Collectors.toList());

        return dtoList;
    }

    // 게시글 작성
    @Transactional
    public Long createArticle(Long memberId, ArticleDTO articleDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Article article = Article.builder()
                .id(articleDTO.getId())
                .articleTitle(articleDTO.getArticleTitle())
                .articleContent(articleDTO.getArticleContent())
                .articleCreated(LocalDateTime.now())
                .viewCount(0L) // 조회수 초기화
                .member(member)
                .build();

        article = articleRepository.save(article);
        return article.getId();
    }

    // 게시글 조회
    @Transactional
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));


        ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
        articleDTO.setMemberId(article.getMember().getId());
        Long commentCount = commentRepository.countByArticle(article);
        articleDTO.setCommentCount(commentCount);

        return articleDTO;
    }



    // 조회수 증가
    @Transactional
    public void incrementViewCount(Long id) {
        articleRepository.incrementViewCount(id);
        articleRepository.flush();
    }

    // 게시글 업데이트
    @Transactional
    public void updateArticle(Long memberId, Long id, ArticleDTO articleDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("글을 찾지 못했습니다." + id));

        // 로그인한 회원이 다를 시에는 다른 회원의 글을 수정할 권한을 주지 않음
        if (!article.getMember().getId().equals(memberId)) {
            throw new RuntimeException("이 글을 수정할 권한이 없습니다.");
        }

        article.setArticleTitle(articleDTO.getArticleTitle());
        article.setArticleContent(articleDTO.getArticleContent());
        article.setArticleUpdated(LocalDateTime.now());
        article.setMember(member);
        articleRepository.save(article);
    }

    // 게시글 삭제
    @Transactional
    public void deleteArticle(Long memberId, Long id) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("글을 찾지 못했습니다. " + id));

        // 로그인한 회원이 admin인 경우 모든 글 삭제 가능
        if (!member.getMemberRole().equals("Admin") && !article.getMember().getId().equals(memberId)) {
            throw new RuntimeException("이 글을 삭제할 권한이 없습니다.");
        }

        articleRepository.deleteById(id);
    }

    // 회원 마이페이지 게시글 목록 조회
    public PageResponseDTO<ArticleDTO> listByMember(Long memberId, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), Sort.by("id").descending());

        Page<Article> result = articleRepository.findByMemberId(memberId, pageable);

        List<ArticleDTO> dtoList = result.getContent().stream()
                .map(article -> {
                    ArticleDTO articleDTO = modelMapper.map(article, ArticleDTO.class);
                    Long commentCount = commentRepository.countByArticle(article);
                    articleDTO.setCommentCount(commentCount);
                    return articleDTO;
                })
                .collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ArticleDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(totalCount)
                .build();
    }
}