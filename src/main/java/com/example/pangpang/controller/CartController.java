package com.example.pangpang.controller;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.CartDTO;
import com.example.pangpang.dto.CartListDTO;
import com.example.pangpang.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    

    private final CartService cartService;


    // 나중에 장바구니 누르면 그 로그인한 사용자의 장바구니가 보이게 해야함
    @GetMapping("")
    public List<CartListDTO> list(){

        return cartService.list();
    }

    // 나중에 로그인한 사용자 id를 넘겨주어야함
    // 지금은 cartDTO안에 1로 고정되있음
    @PostMapping("")
    public Map<String, String> add(@Valid @RequestBody CartDTO cartDTO){

        cartService.add(cartDTO);

        return Map.of("result", "추가 완료");
    }


    // 나중에 로그인한 사용자 id를 넘겨주어야함
    // 지금은 1로 강제로 고정
    @DeleteMapping("")
    public Map<String, String> delete(@RequestBody CartListDTO cartListDTO){

        Long memberId = 1L;

        cartService.delete(memberId, cartListDTO);

        return Map.of("result", "삭제 완료");
    }
    
}
