package com.example.pangpang.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.pangpang.security.JwtAuthenticationFilter;
import com.example.pangpang.service.UserDetailsServiceImpl;

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
                                .requestMatchers(
                                                "/**", // 임시허용
                                                "/", // 메인 페이지
                                                "/api/member/**", // 로그인, 회원가입, 아이디찾기, 비밀번호찾기
                                                "/api/article/list", "/api/article/read/**", // 자유게시판 리스트, 상세보기
                                                "/api/product/list", "/api/product/read/**") // 상품 리스트, 상품 상세보기
                                .permitAll() // 여기는 인증 없이 접근 가능

                                .requestMatchers("/api/mypage/**", "/api/orders/**").authenticated() // 마이페이지, 장바구니

                                .requestMatchers("/manager/**").hasRole("ADMIN") // 관리자 경로
                                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // 사용자 경로
                                .anyRequest().authenticated() // 나머지는 모두 인증 필요
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

        // 권한 빈으로 등록
        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http,
                        BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsServiceImpl userDetailService)
                        throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class)
                                .userDetailsService(userDetailsService)
                                .passwordEncoder(bCryptPasswordEncoder)
                                .and()
                                .build();
        }
}
