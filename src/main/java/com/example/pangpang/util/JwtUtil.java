package com.example.pangpang.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
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

    // 만료시간은 밀리초 단위
    // 초 밀리초
    private long expiration = 10 * 1000; // 토큰 만료 시간 (30초)

    // 토큰 생성
    public String generateToken(String memberId, Long id, String memberName, String memberNickname, String memberRole,
            boolean isActive) {
        Claims claims = Jwts.claims().setSubject(memberId);
        claims.put("id", id);
        claims.put("memberName", memberName);
        claims.put("memberNickname", memberNickname);
        claims.put("memberRole", memberRole);
        claims.put("isActive", isActive);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            // 만료된 토큰
            throw new RuntimeException("Token expired", e);

        } catch (UnsupportedJwtException | MalformedJwtException e) {
            // 잘못된 토큰
            throw new RuntimeException("Unsupported or malformed token", e);

        } catch (Exception e) {
            // 기타 예외 처리
            throw new RuntimeException("Token error", e);
        }
    }

    // 토큰 유효기간(참/거짓)
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = parseToken(token);
            final String username = claims.getSubject();
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (RuntimeException e) {
            return false;
        }
    }

    // 토큰이 만료되었는지 확인(참/거짓)
    private boolean isTokenExpired(String token) {
        final Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
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
}
