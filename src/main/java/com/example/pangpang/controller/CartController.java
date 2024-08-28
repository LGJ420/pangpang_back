package com.example.pangpang.controller;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.CartDTO;
import com.example.pangpang.dto.CartListDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    

    private final CartService cartService;


    // 나중에 장바구니 누르면 그 로그인한 사용자의 장바구니가 보이게 해야함
    @GetMapping("")
    public ResponseEntity<List<CartListDTO>> list(Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        return ResponseEntity.ok().body(cartService.list(memberId));
    }


    @PostMapping("")
    public ResponseEntity<Map<String, String>> add(
        @RequestBody CartDTO cartDTO,
        Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        cartService.add(memberId, cartDTO);

        return ResponseEntity.ok().body(Map.of("result", "success"));
    }


    @DeleteMapping("")
    public ResponseEntity<Map<String, String>> delete(
        @RequestBody CartListDTO cartListDTO,
        Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();    

        cartService.delete(memberId, cartListDTO);

        return ResponseEntity.ok().body(Map.of("result", "success"));
    }
    


    @PutMapping("")
    public ResponseEntity<Map<String, String>> update(
        @RequestBody CartListDTO cartListDTO,
        Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();
    
        cartService.update(memberId, cartListDTO);

        return ResponseEntity.ok().body(Map.of("result", "success"));
    }



    // 테스트용
    @GetMapping("/test")
    public Map<String, Long> test(Authentication auth){

        Member member = (Member)auth.getPrincipal();
        Long memberId = member.getId();

        return Map.of("result", memberId);
    }
    
}
