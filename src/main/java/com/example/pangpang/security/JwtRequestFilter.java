package com.example.pangpang.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// OncePerRequestFilter를 확장한 것으로, 이 필터는 각 요청에 대해 한 번만 실행 
// Spring Security에서 JWT를 사용할 때 주로 이 방식을 사용
public class JwtRequestFilter extends OncePerRequestFilter {

    // JWT 서명 시 사용하는 비밀키
    private final String secretKey = "your_secret_key";

    @Override
    //필터가 요청을 처리할 때 호출
    // JWT 검증 및 필요한 정보 추출
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 요청 헤더(JWT 토큰 포함)에서 Authorization 값 가져옴
        final String authorizationHeader = request.getHeader("Authorization");

        String memberId = null;
        String jwt = null;

        // Bearer을 사용하여 토큰 추출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            // Jwts.parserBuilder()를 사용하여 검증
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))) // 비밀 키로 서명 확인
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            
            memberId = claims.getSubject(); // JWT의 사용자 추출
        }

        // 필터 체인 계속 진행
        chain.doFilter(request, response);
    }
}
