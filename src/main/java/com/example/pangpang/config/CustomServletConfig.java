package com.example.pangpang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 잘보면 Mvc관련 상속인게 보입니다
 * 서블릿에 필요한 설정들 해놓은곳입니다
 */
@Configuration
public class CustomServletConfig implements WebMvcConfigurer {
    

    /*
     * 이거 cors정책 설정인데
     * 이거 설정 안해놓고
     * 프론트에서 axios하면 안받아줍니다
     * 이게 이런 비동기 통신하면 무조건 cors정책을 설정해야 됩니다
     */
    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
            .maxAge(300)
            .allowedHeaders("Authorization", "Cache-Control", "Content-Type");
    }
}
