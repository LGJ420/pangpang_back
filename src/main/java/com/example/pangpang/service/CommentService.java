package com.example.pangpang.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pangpang.entity.Comment;
import com.example.pangpang.repository.CommentRepository;

import java.util.*;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(Comment comment){
        return commentRepository.save(comment);
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id){
        return commentRepository.findById(id);
    }

    public Optional<Comment> updateComment(Long id, Comment commentDetails){
        if (commentRepository.existsById(id)){
            commentDetails.setId(id);
            return Optional.of(commentRepository.save(commentDetails));
        }
        return Optional.empty();
    }

    public boolean deleteComment(Long id){
        if (commentRepository.existsById(id)){
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
