package com.example.pangpang.controller;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import com.example.pangpang.dto.CartDTO;
import com.example.pangpang.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    

    private final CartService cartService;


    @GetMapping("")
    public List<CartDTO> list(){

        return cartService.list();
    }


    @PostMapping("")
    public Map<String, String> add(@Valid @RequestBody CartDTO cartDTO){

        cartService.add(cartDTO);

        return Map.of("result", "추가 완료");
    }
    
}
