package com.example.pangpang.controller;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangpang.dto.OrdersDTO;
import com.example.pangpang.service.OrdersService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {
    
    private final OrdersService ordersService;


    @GetMapping("/list")
    public List<OrdersDTO> getList(){

        return ordersService.list();

    }


    @PostMapping("")
    public Map<String, String> add(@Valid @RequestBody OrdersDTO ordersDTO){

        //지금은 유저를 1도 지정
        Long memberId = 1L;

        ordersService.add(memberId, ordersDTO);

        return Map.of("result", "ok");
    }
}
