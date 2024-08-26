package com.example.pangpang.controller;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Long> createComment(@Valid @RequestBody CommentDTO commentDTO, Authentication auth){
        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        Long commentId = commentService.createComment(memberId, commentDTO.getArticleId(), commentDTO);
        return ResponseEntity.ok(commentId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO commentDTO = commentService.getCommentById(id);
        return ResponseEntity.ok(commentDTO);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<Page<CommentDTO>> getCommentsByArticleId(
        @PathVariable Long articleId,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "5") int size) {

        return ResponseEntity.ok(commentService.getCommentsByArticleId(articleId, page, size));
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable Long id, @Valid @RequestBody CommentDTO commentDTO, Authentication auth){
        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        commentService.updateComment(memberId, id, commentDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myComments")
    public ResponseEntity<PageResponseDTO<CommentDTO>> getCommentsByMemberId(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "5") int size,
        Authentication auth) {

        Member member = (Member) auth.getPrincipal();
        Long memberId = member.getId();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .build();

        PageResponseDTO<CommentDTO> responseDTO = commentService.getCommentsByMemberId(memberId, pageRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }




    /* 공지사항 댓글 불러오기*/
    @GetMapping("/notice/{id}")
    public ResponseEntity<PageResponseDTO<CommentDTO>> getNoticeComment(
        @PathVariable(name = "id") Long noticeId,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "5") int size){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .page(page)
            .size(size)
            .build();

        PageResponseDTO<CommentDTO> pageResponseDTO =
            commentService.getNoticeComment(noticeId, pageRequestDTO);

        return ResponseEntity.ok().body(pageResponseDTO);
    }


    /* 공지사항 댓글 쓰기*/
    @PostMapping("/notice/{id}")
    public ResponseEntity<Map<String, String>> addNoticeComment(
        @PathVariable(name = "id") Long noticeId,
        Authentication auth,
        @RequestBody CommentDTO commentDTO){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        commentService.addNoticeComment(noticeId, memberId, commentDTO);

        return ResponseEntity.ok().body(null);
    }


    /* 공지사항 댓글 수정 작업중*/
    @PutMapping("/notice/{id}")
    public ResponseEntity<Map<String, String>> modifyNoticeComment(
        Authentication auth,
        @PathVariable(name = "id") Long id){


        return ResponseEntity.ok().body(null);
    }


    /* 공지사항 댓글 삭제 작업중*/
    @DeleteMapping("/notice/{id}")
    public ResponseEntity<Map<String, String>> deleteNoticeComment(
        Authentication auth,
        @PathVariable(name = "id") Long id){

        return ResponseEntity.ok().body(null);
    }



}
