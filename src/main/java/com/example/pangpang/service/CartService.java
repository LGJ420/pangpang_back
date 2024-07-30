package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.pangpang.dto.CartDTO;
import com.example.pangpang.entity.Cart;
import com.example.pangpang.entity.Member;
import com.example.pangpang.entity.Product;
import com.example.pangpang.repository.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public void add(CartDTO cartDTO){

        Member member = memberRepository.findById(cartDTO.getMemberId())
            .orElseThrow(()->new EntityNotFoundException("Member not found"));
        Product product = productRepository.findById(cartDTO.getProductId())
            .orElseThrow(()->new EntityNotFoundException("Product not found"));

        Cart findCart = cartRepository.findByMemberAndProduct(member, product).orElse(null);


        if (findCart != null) {
            
            int cartCount = findCart.getCartCount() + cartDTO.getCartCount();

            findCart.changeCartCount(cartCount);

            cartRepository.save(findCart);
        }
        else {

            Cart cart = Cart.builder()
                .cartCount(cartDTO.getCartCount())
                .member(member)
                .product(product)
                .build();

            cartRepository.save(cart);
        }
    }


    public List<CartDTO> list(){

        List<CartDTO> cartDTOs = cartRepository.findAll()
            .stream()
            .map(cart->CartDTO.builder()
                .cartCount(cart.getCartCount())
                .memberId(cart.getMember().getId())
                .productId(cart.getProduct().getId())
                .build())
            .collect(Collectors.toList());
        
        return cartDTOs;
    }
}
