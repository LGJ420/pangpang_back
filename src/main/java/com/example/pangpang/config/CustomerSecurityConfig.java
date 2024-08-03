package com.example.pangpang.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class CustomerSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("--------------------security config--------------------");

        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        // ▼▼▼ 세션관리 무상태(STATELESS)로 설정 ▼▼▼
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // ▲▲▲ 세션관리 무상태(STATELESS)로 설정 ▲▲▲

        // ▼▼▼ CSRF 보호 비활성화 코드(☆★☆★배포 시 삭제하기!!!!!!!!!!!!!!!!!!!!☆★☆★) ▼▼▼
        http.csrf(config -> config.disable());
        // ▲▲▲ CSRF 보호 비활성화 코드(☆★☆★배포 시 삭제하기!!!!!!!!!!!!!!!!!!!!☆★☆★) ▲▲▲

        // ▼▼▼ 경로 허용 설정 ▼▼▼
        // 로그인, 회원가입, 아이디찾기, 비밀번호 찾기->비밀번호 변경 경로 허용
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/api/member/**").permitAll()
                .anyRequest().authenticated());
        // ▲▲▲ 경로 허용 설정 ▲▲▲

        // 스프링 시큐리티는 username, password를 쓰게 강요하는데, 그 이름을 내가 쓰는 DTO이름으로 바꿔준 것
        http.formLogin(login -> login
                // .loginPage("/api/member/login")
                // .loginProcessingUrl("/api/member/login")
                .usernameParameter("memberIdInLogin")
                .passwordParameter("memberPwInLogin"));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // ▼▼▼ 비밀번호 암호화 ▼▼▼
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // ▲▲▲ 비밀번호 암호화 ▲▲▲
}
