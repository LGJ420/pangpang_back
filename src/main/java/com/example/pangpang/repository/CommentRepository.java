package com.example.pangpang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pangpang.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    Page<Comment> findByArticleId(Long articleId, Pageable pageable);

    Page<Comment> findByMemberId(Long memberId, Pageable pageable);
}
