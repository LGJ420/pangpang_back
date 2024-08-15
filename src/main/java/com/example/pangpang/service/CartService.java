package com.example.pangpang.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.pangpang.dto.*;
import com.example.pangpang.entity.*;
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

    public void add(Long memberId, CartDTO cartDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        Product product = productRepository.findById(cartDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Cart findCart = cartRepository.findByMemberAndProduct(member, product).orElse(null);

        if (findCart != null) {

            int cartCount = findCart.getCartCount() + cartDTO.getCartCount();

            findCart.changeCartCount(cartCount);

            cartRepository.save(findCart);
        } else {

            Cart cart = Cart.builder()
                    .cartCount(cartDTO.getCartCount())
                    .member(member)
                    .product(product)
                    .build();

            cartRepository.save(cart);
        }
    }

    public List<CartListDTO> list(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다"));

        List<CartListDTO> cartListDTOs = cartRepository.findByMember(member)
                .stream()
                .map(cart -> {

                    // 상품 이미지 리스트 초기화
                    List<String> uploadFileNames = new ArrayList<>();

                    // 장바구니에 담긴 상품 이미지 리스트가 null인지 아닌지 확인
                    if(cart.getProduct().getProductImage() != null) {

                        // 상품 이미지 리스트 순회
                       for(ProductImage productImage : cart.getProduct().getProductImage()) {
                        
                        // .getFileName() 메서드 호출해 파일 이름 가져오기
                        // 가져온 파일 이름을 uploadFileNames에 리스트에 추가    
                        uploadFileNames.add(productImage.getFileName());
                       } 
                    } else {
                        uploadFileNames = Collections.emptyList();      // 만약 이미지 목록 null이면 빈 리스트 사용
                    }
      

                    return CartListDTO.builder() // return을 추가합니다
                            .productId(cart.getProduct().getId())
                            .productTitle(cart.getProduct().getProductTitle())
                            .productContent(cart.getProduct().getProductContent())
                            .productPrice(cart.getProduct().getProductPrice())
                            .cartCount(cart.getCartCount())
                            .uploadFileNames(uploadFileNames)
                            .build();
                })
                .collect(Collectors.toList());

                return cartListDTOs;
    }

    public void delete(Long memberId, CartListDTO cartListDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Product product = productRepository.findById(cartListDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        cartRepository.deleteByMemberAndProduct(member, product);
    }

    public void update(Long memberId, CartListDTO cartListDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Product product = productRepository.findById(cartListDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Cart findCart = cartRepository.findByMemberAndProduct(member, product).orElse(null);

        findCart.changeCartCount(cartListDTO.getCartCount());

        cartRepository.save(findCart);

    }
}
