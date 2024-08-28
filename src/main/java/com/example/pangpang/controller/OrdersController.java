package com.example.pangpang.controller;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.OrdersDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.OrdersService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {
    
    private final OrdersService ordersService;


    @GetMapping("/list")
    public ResponseEntity<List<OrdersDTO>> getList(
        @RequestParam(value = "search", required = false) String search,
        Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();
    
        return ResponseEntity.ok().body(ordersService.list(memberId, search));
    }


    @PostMapping("")
    public ResponseEntity<Map<String, String>> add(
        @Valid @RequestBody OrdersDTO ordersDTO,
        Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        ordersService.add(memberId, ordersDTO);

        return ResponseEntity.ok().body(Map.of("result", "success"));
    }
}
