package com.example.pangpang.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.*;
import com.example.pangpang.repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;


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


    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        CommentDTO dto = modelMapper.map(comment, CommentDTO.class);
        dto.setMemberNickname(comment.getMember().getMemberNickname());
        return dto;
    }


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




    /* 공지사항 댓글 불러오기*/
    public PageResponseDTO<CommentDTO> getNoticeComment(Long noticeId, PageRequestDTO pageRequestDTO){

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), Sort.by("commentCreated").descending());

        Page<Comment> commentPage = commentRepository.findByNoticeId(noticeId, pageable);

        List<CommentDTO> dtoList = commentPage.getContent().stream()
            .map(comment -> {
                CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                commentDTO.setMemberNickname(comment.getMember().getMemberNickname());
                return commentDTO;
            })
            .collect(Collectors.toList());
        
        PageResponseDTO<CommentDTO> responseDTO = PageResponseDTO.<CommentDTO>withAll()
            .dtoList(dtoList)
            .pageRequestDTO(pageRequestDTO)
            .totalCount(commentPage.getTotalElements())
            .build();
        
        return responseDTO;
    }
    


    /* 공지사항 댓글 쓰기*/
    public void addNoticeComment(Long noticeId, Long memberId, CommentDTO commentDTO){

        Notice notice = noticeRepository.findById(noticeId)
            .orElseThrow(() -> new EntityNotFoundException("Notice not found"));
            
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        
        Comment comment = Comment.builder()
            .commentContent(commentDTO.getCommentContent())
            .commentCreated(LocalDateTime.now())
            .notice(notice)
            .member(member)
            .build();

        commentRepository.save(comment);

    }
        

    /* 공지사항 댓글 수정*/
    public void modifyNoticeComment(Long memberId, CommentDTO commentDTO){

        Comment comment = commentRepository.findById(commentDTO.getId())
            .orElseThrow(() -> new EntityNotFoundException("Comment not found"));


        if(memberId != comment.getMember().getId()){

            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
        }

        comment.setCommentContent(commentDTO.getCommentContent());
        comment.setCommentUpdated(LocalDateTime.now());
        
        commentRepository.save(comment);
    }



    /* 공지사항 댓글 삭제*/
    public void deleteNoticeComment(Long memberId, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    
        if (memberId != comment.getMember().getId()) {
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
        }
    
        commentRepository.delete(comment);
    }

}