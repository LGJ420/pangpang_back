package com.example.pangpang.controller;

import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.ProductReviewDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.ProductReviewService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/productreview")
@RequiredArgsConstructor
public class ProductReviewController {
    
    private final ProductReviewService productReviewService;

    @PostMapping("")
    public Map<String, String> add(
        @ModelAttribute ProductReviewDTO productReviewDTO,
        Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();
    
        productReviewService.add(memberId, productReviewDTO);

        return Map.of("result", "등록 완료");
    }
}
