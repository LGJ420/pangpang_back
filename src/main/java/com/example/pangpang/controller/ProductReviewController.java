package com.example.pangpang.controller;

import java.util.*;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.ProductReviewDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.ProductReviewService;
import com.example.pangpang.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/productreview")
@RequiredArgsConstructor
public class ProductReviewController {
    
    private final ProductReviewService productReviewService;
    private final CustomFileUtil customFileUtil;

    @PostMapping("")
    public ResponseEntity<Map<String, String>> add(
        @ModelAttribute ProductReviewDTO productReviewDTO,
        Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();
    
        productReviewService.add(memberId, productReviewDTO);

        return ResponseEntity.ok().body(Map.of("result", "success"));
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<ProductReviewDTO>> list(@PathVariable(name = "id") Long productId){

        return ResponseEntity.ok().body(productReviewService.list(productId));
    }


    @GetMapping
    public ResponseEntity<List<ProductReviewDTO>> mylist(Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        return ResponseEntity.ok().body(productReviewService.mylist(memberId));
    }


    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        
        return customFileUtil.getFile(fileName);
    }
}
