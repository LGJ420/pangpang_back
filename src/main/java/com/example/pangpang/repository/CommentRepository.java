package com.example.pangpang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pangpang.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByArticleId(Long articleId);
}
