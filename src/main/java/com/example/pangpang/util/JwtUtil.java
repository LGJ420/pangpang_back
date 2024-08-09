package com.example.pangpang.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import java.util.function.Function;

@Component
public class JwtUtil {
    private SecretKey secretKey = Keys.hmacShaKeyFor("yourSecretKeyyourSecretKeyyourSecretKey".getBytes());
    // 키는 최소 32바이트 이상

    private long expiration = 86400000; // 토큰 만료 시간 (예: 24시간)

    // 토큰 생성
    public String generateToken(String memberId, Long id, String memberName, String memberRole) {
        Claims claims = Jwts.claims().setSubject(memberId);
        claims.put("id", id);
        claims.put("memberName", memberName);
        claims.put("memberRole", memberRole);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효기간(참/거짓)
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = parseToken(token).getSubject();
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 토큰이 만료되었는지 확인(참/거짓)
    private boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    // JWT 토큰에서 사용자 이름을 추출
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 위의 메소드를 위해 이 메소드가 실행됨
    // 여기서 토큰의 모든 클레임을 가져옴->위 메소드에서 사용자 이름을 추출함
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    // private Claims getAllClaimsFromToken(String token) {
    // return Jwts.parserBuilder()
    // .setSigningKey(secretKey)
    // .build()
    // .parseClaimsJws(token)
    // .getBody();
    // }
}
