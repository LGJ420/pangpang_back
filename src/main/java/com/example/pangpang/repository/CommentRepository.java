package com.example.pangpang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pangpang.entity.Article;
import com.example.pangpang.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    Page<Comment> findByArticleId(Long articleId, Pageable pageable);

    Page<Comment> findByMemberId(Long memberId, Pageable pageable);

    // Method to count comments by article
    Long countByArticle(Article article);
}
