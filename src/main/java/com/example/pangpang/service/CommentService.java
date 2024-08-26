package com.example.pangpang.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.CommentDTO;
import com.example.pangpang.dto.PageRequestDTO;
import com.example.pangpang.dto.PageResponseDTO;
import com.example.pangpang.entity.Comment;
import com.example.pangpang.entity.Member;
import com.example.pangpang.entity.Article;
import com.example.pangpang.repository.CommentRepository;
import com.example.pangpang.repository.MemberRepository;
import com.example.pangpang.repository.ArticleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public Long createComment(Long memberId, Long articleId, CommentDTO commentDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        Comment comment = Comment.builder()
                .commentContent(commentDTO.getCommentContent())
                .commentCreated(LocalDateTime.now())
                .article(article)
                .member(member)
                .build();

        comment = commentRepository.save(comment);
        return comment.getId();
    }

    public Page<CommentDTO> getCommentsByArticleId(Long articleId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("commentCreated").descending());

        Page<Comment> commentPage = commentRepository.findByArticleId(articleId, pageable);

        return commentPage.map(comment -> {
            CommentDTO dto = modelMapper.map(comment, CommentDTO.class);
            dto.setMemberNickname(comment.getMember().getMemberNickname());
            return dto;
        });
    }

    @Transactional
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        CommentDTO dto = modelMapper.map(comment, CommentDTO.class);
        dto.setMemberNickname(comment.getMember().getMemberNickname());
        return dto;
    }

    @Transactional
    public void updateComment(Long memberId, Long id, CommentDTO commentDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getMember().getId().equals(memberId)) {
            throw new RuntimeException("You don't have permission to update this comment.");
        }

        comment.setCommentContent(commentDTO.getCommentContent());
        comment.setCommentUpdated(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found.");
        }
        commentRepository.deleteById(id);
    }

    public PageResponseDTO<CommentDTO> getCommentsByMemberId(Long memberId, PageRequestDTO pageRequestDTO) {
        // Create pageable object from pageRequestDTO
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), Sort.by("commentCreated").descending());
    
        // Fetch comments using repository
        Page<Comment> result = commentRepository.findByMemberId(memberId, pageable);
    
        List<CommentDTO> dtoList = result.getContent().stream()
        .map(comment -> {
            CommentDTO dto = modelMapper.map(comment, CommentDTO.class);
            dto.setViewCount(comment.getArticle().getViewCount());  // Set the viewCount from Article
            return dto;
        })
        .collect(Collectors.toList());
    
        // Return PageResponseDTO with pagination information
        return PageResponseDTO.<CommentDTO>withAll()
            .dtoList(dtoList)
            .pageRequestDTO(pageRequestDTO)
            .totalCount(result.getTotalElements())
            .build();
    }
}