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
import lombok.extern.log4j.Log4j2;

@Log4j2
@Transactional
@Service
@RequiredArgsConstructor
public class OrdersService {

        private final OrdersRepository ordersRepository;
        private final MemberRepository memberRepository;
        private final ProductRepository productRepository;
        private final CartRepository cartRepository;
        private final ProductReviewRepository productReviewRepository;
        private final ModelMapper modelMapper;

        public List<OrdersDTO> list(Long memberId, String search) {

                Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

                // Sort 객체를 생성하여 주문 날짜 기준으로 내림차순 정렬
                Sort sort = Sort.by("orderDate").descending();
                List<Orders> orders = ordersRepository.findByMember(member, sort);

                return orders.stream()
                                .map(order -> {
                                        OrdersDTO dto = modelMapper.map(order, OrdersDTO.class);

                                        List<OrdersProductDTO> productDTOs = order.getOrdersProducts().stream()
                                                        .map(ordersProduct -> {
                                                                OrdersProductDTO productDTO = modelMapper.map(
                                                                                ordersProduct, OrdersProductDTO.class);

                                                                // 이미지 리스트 수동 매핑
                                                                List<String> imageFileNames = ordersProduct.getProduct()
                                                                                .getProductImage().stream()
                                                                                .map(productImage -> productImage
                                                                                                .getFileName())
                                                                                .collect(Collectors.toList());

                                                                productDTO.setUploadFileNames(imageFileNames);

                                                                // 리뷰 작성 여부 확인
                                                                boolean reviewExist = productReviewRepository
                                                                                .existsByProductIdAndMemberId(
                                                                                                ordersProduct.getProduct()
                                                                                                                .getId(),
                                                                                                memberId);
                                                                productDTO.setReviewExist(reviewExist);

                                                                return productDTO;
                                                        })
                                                        .filter(productDTO -> search == null || search.isBlank()
                                                                        || productDTO.getProductTitle()
                                                                                        .contains(search))
                                                        .collect(Collectors.toList());

                                        dto.setOrdersProducts(productDTOs);

                                        return dto;
                                })
                                .filter(dto -> !dto.getOrdersProducts().isEmpty()) // 제품 목록이 비어있지 않은 OrdersDTO만 유지
                                .collect(Collectors.toList());
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

                // 상품 업데이트를 위한 Map 생성 (중복방지)
                Map<Long, Product> productsToUpdate = new HashMap<>();

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

                                        // 상품 총 판매량 업데이트
                                        product.setProductTotalSales(
                                                        product.getProductTotalSales() + dto.getCartCount());

                                        // 업데이트할 상품을 Map에 추가
                                        productsToUpdate.put(product.getId(), product);

                                        log.info("product Result: ID = {}, productTitle = {}, price = {}, image = {}",
                                                        product.getId(),
                                                        product.getProductTitle(),
                                                        product.getProductPrice(),
                                                        product.getProductImage() != null
                                                                        ? product.getProductImage().size()
                                                                        : 0); // productImage 필드가 출력됨

                                        cartRepository.deleteByMemberAndProduct(member, product);
                                        return ordersProduct;
                                })
                                .collect(Collectors.toList());

                // log.info("ordersProducts result : " + ordersProducts);

                // 이어준 상품들을 생성한 주문 내역에 다시 설정
                orders.addOrdersProducts(ordersProducts);

                // 저장
                ordersRepository.save(orders);

        }
}