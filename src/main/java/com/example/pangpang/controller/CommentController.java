package com.example.pangpang.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.CommentDTO;
import com.example.pangpang.dto.PageRequestDTO;
import com.example.pangpang.dto.PageResponseDTO;
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
}
