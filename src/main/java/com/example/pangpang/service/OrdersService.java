package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.OrdersDTO;
import com.example.pangpang.entity.Member;
import com.example.pangpang.entity.Orders;
import com.example.pangpang.entity.OrdersProduct;
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


    public void add(Long memberId, OrdersDTO ordersDTO){

        Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Orders orders = Orders.builder()
            .member(member)
            .orderName(ordersDTO.getName())
            .orderPhone(ordersDTO.getPhone())
            .orderAddress(ordersDTO.getAddress())
            .build();

        List<OrdersProduct> ordersProducts = ordersDTO.getOrdersProducts().stream()
            .map(dto -> {

                Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

                OrdersProduct ordersProduct = OrdersProduct.builder()
                    .orders(orders)
                    .product(product)
                    .count(dto.getCartCount())
                    .build();
                    
                return ordersProduct;

            })
            .collect(Collectors.toList());


        orders.addOrdersProducts(ordersProducts);
        ordersRepository.save(orders);

    }
}