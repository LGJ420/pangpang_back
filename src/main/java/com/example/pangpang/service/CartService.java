package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.pangpang.dto.CartDTO;
import com.example.pangpang.dto.CartListDTO;
import com.example.pangpang.entity.Cart;
import com.example.pangpang.entity.Member;
import com.example.pangpang.entity.Product;
import com.example.pangpang.repository.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public void add(Long memberId, CartDTO cartDTO){

        Member member = memberRepository.findById(memberId)
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




    public List<CartListDTO> list(Long memberId){

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다"));

        List<CartListDTO> cartListDTOs = cartRepository.findByMember(member)
            .stream()
            .map(cart->CartListDTO.builder()
                .productId(cart.getProduct().getId())
                .productTitle(cart.getProduct().getProductTitle())
                .productContent(cart.getProduct().getProductContent())
                .productPrice(cart.getProduct().getProductPrice())
                .cartCount(cart.getCartCount())
                .build())
                
            .collect(Collectors.toList());
        
        return cartListDTOs;
    }

    


    public void delete(Long memberId, CartListDTO cartListDTO){

        Member member = memberRepository.findById(memberId)
            .orElseThrow(()->new EntityNotFoundException("Member not found"));
        
        Product product = productRepository.findById(cartListDTO.getProductId())
            .orElseThrow(()->new EntityNotFoundException("Product not found"));

        cartRepository.deleteByMemberAndProduct(member, product);
    }







    public void update(Long memberId, CartListDTO cartListDTO){

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Product product = productRepository.findById(cartListDTO.getProductId())
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Cart findCart = cartRepository.findByMemberAndProduct(member, product).orElse(null);

        findCart.changeCartCount(cartListDTO.getCartCount());

        cartRepository.save(findCart);

    }
}
