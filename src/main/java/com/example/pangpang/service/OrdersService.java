package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangpang.dto.OrdersDTO;
import com.example.pangpang.dto.OrdersProductDTO;
import com.example.pangpang.entity.*;
import com.example.pangpang.repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class OrdersService {

        private final OrdersRepository ordersRepository;
        private final MemberRepository memberRepository;
        private final ProductRepository productRepository;
        private final CartRepository cartRepository;
        private final ModelMapper modelMapper;

        public List<OrdersDTO> list(Long memberId, String search) {

                Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

                // Sort 객체를 생성하여 주문 날짜 기준으로 내림차순 정렬
                Sort sort = Sort.by("orderDate").descending();
                List<Orders> orders = ordersRepository.findByMember(member, sort);

                if (search != null && !search.isBlank()) {

                        List<OrdersDTO> ordersDTOs = orders.stream()
                                        .map(order -> {
                                                OrdersDTO dto = modelMapper.map(order, OrdersDTO.class);
                                                // 필터링 로직 추가
                                                List<OrdersProductDTO> filteredProducts = dto.getOrdersProducts()
                                                                .stream()
                                                                .filter(product -> product.getProductTitle()
                                                                                .contains(search))
                                                                .collect(Collectors.toList());
                                                dto.setOrdersProducts(filteredProducts); // 필터링된 제품 목록으로 업데이트
                                                return dto;

                                        })
                                        .filter(dto -> !dto.getOrdersProducts().isEmpty()) // 제품 목록이 비어있지 않은 OrdersDTO만
                                                                                           // 유지
                                        .collect(Collectors.toList());

                        return ordersDTOs;
                } else {

                        List<OrdersDTO> ordersDTOs = orders
                                        .stream()
                                        .map(order -> modelMapper.map(order, OrdersDTO.class))
                                        .collect(Collectors.toList());

                        return ordersDTOs;
                }
        }

        public void add(Long memberId, OrdersDTO ordersDTO) {

                Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

                Orders orders = Orders.builder()
                                .member(member)
                                .orderName(ordersDTO.getName())
                                .orderPhone(ordersDTO.getPhone())
                                .orderAddress(ordersDTO.getAddress())
                                .build();

                // 한번 주문할때 상품이 많으므로 map을 돌려서 상품을 하나하나 주문내역에 이어준다
                List<OrdersProduct> ordersProducts = ordersDTO.getOrdersProducts().stream()
                                .map(dto -> {

                                        // 상품 찾기
                                        Product product = productRepository.findById(dto.getProductId())
                                                        .orElseThrow(() -> new EntityNotFoundException(
                                                                        "Product not found"));

                                        // 주문내역과 상품 이어주기
                                        OrdersProduct ordersProduct = OrdersProduct.builder()
                                                        .orders(orders)
                                                        .product(product)
                                                        .count(dto.getCartCount())
                                                        .build();

                                        cartRepository.deleteByMemberAndProduct(member, product);
                                        return ordersProduct;
                                })
                                .collect(Collectors.toList());

                // 이어준 상품들을 생성한 주문 내역에 다시 설정
                orders.addOrdersProducts(ordersProducts);
                
                // 저장
                ordersRepository.save(orders);

        }
}