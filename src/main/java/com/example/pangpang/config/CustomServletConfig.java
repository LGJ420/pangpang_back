package com.example.pangpang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.pangpang.controller.formatter.LocalDateFomatter;



/**
 * 잘보면 Mvc관련 상속인게 보입니다
 * 서블릿에 필요한 설정들 해놓은곳입니다
 * 맨날 말하고 또말하지만
 * 김영한 데려와도 지금 밑에 이걸
 * 외워서 죽어도 못칩니다 (그사람은 그냥 코딩때려치우고 서울대법대가야함)
 * 이런 원리가 있다는걸 알고, 구글링이나 책복붙하시면됩니다
 * 
 * 
 * 그니까 혹시 이걸보고 외우려고
 * ".allowedOrigins랑~ 어~ .allowedMethods("HEAD", "GET", 그리고 어~"
 * 이러는 친구는 없길 바랍니다
 * 
 * 
 * 참고로 원리는 외워야됩니다 ^^
 * (원리도 안외우면 그건 선넘었음)
 */
@Configuration
public class CustomServletConfig implements WebMvcConfigurer {
    





    /*
     * 날짜 포멧입니다
     * 여러분 포멧아시죠?
     * 컴퓨터 포멧 말구요
     * 형식이라고 생각하시면됩니다
     * 자세한건 ctrl를 꾹 눌러서 LocalDateFomatter() 클릭!
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addFormatter(new LocalDateFomatter());
    }




    /*
     * 이거 cors정책 설정인데
     * 이거 설정 안해놓고
     * 프론트에서 axios하면 안받아줍니다
     * 이게 이런 비동기 통신하면 무조건 cors정책을 설정해야 된답니다
     * 이거 하나 안해서 5시간 개고생했었습니다
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
            .maxAge(300)
            .allowedHeaders("Authorization", "Cache-Control", "Content-Type");
    }
}
