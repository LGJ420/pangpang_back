package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.OrdersDTO;
import com.example.pangpang.entity.Orders;
import com.example.pangpang.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class OrdersService {
    
    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;

    public List<OrdersDTO> list(){

        List<Orders> orders = ordersRepository.findAll();
        List<OrdersDTO> ordersDTOs = orders
            .stream()
            .map(order -> modelMapper.map(order, OrdersDTO.class))
            .collect(Collectors.toList());

        return ordersDTOs;
    }


    public void add(OrdersDTO ordersDTO){

        Orders orders = modelMapper.map(ordersDTO, Orders.class);

        Orders savedOrders = ordersRepository.save(orders);
    }
}
