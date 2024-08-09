package com.example.pangpang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.pangpang.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class CustomerSecurityConfig {

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                log.info("--------------------security config--------------------");

                // 세션관리 무상태(STATELESS)로 설정
                http.sessionManagement(
                                sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                // ▼▼▼ CSRF 보호 비활성화 코드(☆★☆★배포 시 삭제하기!!!!!!!!!!!!!!!!!!!!☆★☆★) ▼▼▼
                http.csrf(config -> config.disable());
                // ▲▲▲ CSRF 보호 비활성화 코드(☆★☆★배포 시 삭제하기!!!!!!!!!!!!!!!!!!!!☆★☆★) ▲▲▲

                // 경로 허용 설정
                http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .requestMatchers("/**").permitAll() // 서버 개설 임시 허용 경로
                                // .requestMatchers("/", // 메인 페이지
                                // "/api/article/list", "/api/article/read/**", // 자유게시판 리스트, 상세보기
                                // "/api/member/**", // 로그인, 회원가입, 아이디찾기, 비밀번호찾기
                                // "/api/product/list", "api/product/read/**") // 상품 리스트, 상품 상세보기
                                // .permitAll() // 실제 쓰일 경로
                                // Role에 따른 역할 부여
                                .requestMatchers("/manager/**").hasRole("ADMIN") // 알아서 ROLE_ADMIN으로 반환
                                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // 알아서 ROLE_USER, ROLE_ADMIN으로
                                                                                         // 반환
                                .anyRequest().authenticated() // 위를 제외한 경로는 모두 권한 식별 필요
                );

                // 스프링 시큐리티는 username, password를 쓰게 강요하는데, 그 이름을 내가 쓰는 DTO이름으로 바꿔준 것
                http.formLogin(login -> login
                                // .loginPage("/api/member/login")
                                // .loginProcessingUrl("/api/member/login")
                                .usernameParameter("memberIdInLogin")
                                .passwordParameter("memberPwInLogin"));

                // 로그아웃 설정 추가
                http.logout(logout -> logout
                                .logoutUrl("/api/member/logout") // 로그아웃 요청 URL
                                .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 URL
                                .invalidateHttpSession(true) // 세션 무효화
                                .clearAuthentication(true)); // 인증 정보 초기화

                // JWT 필터 추가
                http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // ▼▼▼ 비밀번호 암호화 ▼▼▼
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
        // ▲▲▲ 비밀번호 암호화 ▲▲▲
}
