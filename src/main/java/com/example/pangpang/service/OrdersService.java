package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.OrdersDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.entity.Orders;
import com.example.pangpang.entity.Product;
import com.example.pangpang.repository.MemberRepository;
import com.example.pangpang.repository.OrdersRepository;
import com.example.pangpang.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class OrdersService {
    
    private final OrdersRepository ordersRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
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

        

        Member member = memberRepository.findById(ordersDTO.getMemberId())
            .orElseThrow(()->new EntityNotFoundException("Member not found"));
        Product product = productRepository.findById(ordersDTO.getProductId())
            .orElseThrow(()->new EntityNotFoundException("Product not found"));

        Orders findOrders = ordersRepository.findByMemberAndProduct(member, product).orElse(null);



        if (findOrders != null) {
            
            int orderCount = findOrders.getOrderCount()+ordersDTO.getOrderCount();

            findOrders.changeOrderCount(orderCount);

            ordersRepository.save(findOrders);
        }
        else {

            Orders orders = Orders.builder()
                .orderCount(ordersDTO.getOrderCount())
                .orderAddress(ordersDTO.getOrderAddress())
                .orderPhone(ordersDTO.getOrderPhone())
                .member(member)
                .product(product)
                .build();
    
            ordersRepository.save(orders);
        }



    }
}
