package com.example.pangpang.controller.formatter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;


/*
 * 날짜/시간은 브라우저에서는 문자열로 전송되지만
 * 서버에서는 LocalDate 또는 LocalDateTime으로 처리되기 때문에
 * Controller에 formatter패키지를 만들어서 이를 변환해주는 Formatter를 만드는것
 */
public class LocalDateFomatter implements Formatter<LocalDateTime>{

    @Override
    public LocalDateTime parse(String text, Locale locale) {
        
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(object);
    }
    
}
