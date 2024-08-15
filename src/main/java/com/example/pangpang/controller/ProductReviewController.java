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
    public Map<String, String> add(
        @ModelAttribute ProductReviewDTO productReviewDTO,
        Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();
    
        productReviewService.add(memberId, productReviewDTO);

        return Map.of("result", "등록 완료");
    }


    @GetMapping("/{id}")
    public List<ProductReviewDTO> list(@PathVariable(name = "id") Long id){

        return productReviewService.list(id);
    }


    @GetMapping
    public List<ProductReviewDTO> mylist(Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        return productReviewService.mylist(memberId);
    }


    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        
        return customFileUtil.getFile(fileName);
    }
}
