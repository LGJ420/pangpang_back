package com.example.pangpang.controller;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.NoticeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeController {
    
    private final NoticeService noticeService;


    /* 공지사항 목록 보기 */
    @GetMapping("/list")
    public PageResponseDTO<NoticeDTO> list(PageRequestDTO pageRequestDTO){

        return noticeService.list(pageRequestDTO);
    }


    @GetMapping("/{id}")
    public NoticeDTO getOne(@PathVariable(name = "id") Long id) {

        return noticeService.getOne(id);
    }


    @PostMapping("")
    public ResponseEntity<Map<String, String>> createOne(
        Authentication auth,
        @RequestBody NoticeDTO noticeDTO){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        noticeService.createOne(memberId, noticeDTO);

        return ResponseEntity.ok().body(Map.of("result", "success"));
    }

    
    @PutMapping("")
    public ResponseEntity<Map<String, String>> modify(
        Authentication auth,
        @RequestBody NoticeDTO noticeDTO){
        
        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        noticeService.modify(memberId, noticeDTO);

        return ResponseEntity.ok().body(Map.of("result", "success"));
    } 



    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(
        Authentication auth,
        @PathVariable(name = "id") Long noticeId){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        noticeService.delete(memberId, noticeId);

        return ResponseEntity.ok().body(Map.of("result", "success"));
    }




}
