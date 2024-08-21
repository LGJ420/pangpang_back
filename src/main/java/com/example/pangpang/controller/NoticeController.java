package com.example.pangpang.controller;

import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.*;
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

}
