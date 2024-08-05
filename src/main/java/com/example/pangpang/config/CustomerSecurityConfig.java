package com.example.pangpang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.pangpang.security.JwtRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class CustomerSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("--------------------security config--------------------");

        // ▼▼▼ 세션관리 무상태(STATELESS)로 설정 ▼▼▼
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // ▲▲▲ 세션관리 무상태(STATELESS)로 설정 ▲▲▲

        // ▼▼▼ CSRF 보호 비활성화 코드(☆★☆★배포 시 삭제하기!!!!!!!!!!!!!!!!!!!!☆★☆★) ▼▼▼
        http.csrf(config -> config.disable());
        // ▲▲▲ CSRF 보호 비활성화 코드(☆★☆★배포 시 삭제하기!!!!!!!!!!!!!!!!!!!!☆★☆★) ▲▲▲

        // ▼▼▼ 경로 허용 설정 ▼▼▼
        // 로그인, 회원가입, 아이디찾기, 비밀번호 찾기->비밀번호 변경 경로 허용
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/**").permitAll()
                // Role에 따른 역할 부여
                .requestMatchers("/admin/**").hasRole("ADMIN") // "ROLE_ADMIN"을 기대
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // "ROLE_USER"와 "ROLE_ADMIN"을 기대

                .anyRequest().authenticated());
        // ▲▲▲ 경로 허용 설정 ▲▲▲

        // 스프링 시큐리티는 username, password를 쓰게 강요하는데, 그 이름을 내가 쓰는 DTO이름으로 바꿔준 것
        http.formLogin(login -> login
                // .loginPage("/api/member/login")
                // .loginProcessingUrl("/api/member/login")
                .usernameParameter("memberIdInLogin")
                .passwordParameter("memberPwInLogin"));

        // JWT 필터 추가
        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ▼▼▼ 비밀번호 암호화 ▼▼▼
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // ▲▲▲ 비밀번호 암호화 ▲▲▲

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }
}
