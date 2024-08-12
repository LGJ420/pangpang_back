package com.example.pangpang.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.CommentDTO;
import com.example.pangpang.entity.Article;
import com.example.pangpang.entity.Comment;
import com.example.pangpang.repository.ArticleRepository;
import com.example.pangpang.repository.CommentRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public CommentDTO createComment(CommentDTO commentDTO) {
        Article article = articleRepository.findById(commentDTO.getArticleId())
        .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
        .article(article)
        .commentAuthor(commentDTO.getCommentAuthor())
        .commentContent(commentDTO.getCommentContent())
        .commentCreated(LocalDateTime.now())
        .build();

        comment = commentRepository.save(comment);
        return convertToDTO(comment);
    }

    private CommentDTO convertToDTO(Comment comment){
        return CommentDTO.builder()
        .id(comment.getId())
        .articleId(comment.getArticle().getId())
        .commentAuthor(comment.getCommentAuthor())
        .commentContent(comment.getCommentContent())
        .commentCreated(comment.getCommentCreated())
        .build();
    }

    public Optional<CommentDTO> getCommentById(Long id){
        return commentRepository.findById(id).map(this::convertToDTO);
    }

    @Transactional
    public void updateComment(Long id, CommentDTO commentDTO){
        Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));

        comment.setCommentAuthor(commentDTO.getCommentAuthor());
        comment.setCommentContent(commentDTO.getCommentContent());
        comment.setCommentUpdated(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("해당 댓글을 찾을 수 없습니다. ID: " + id);
        }
        commentRepository.deleteById(id);
    }
}