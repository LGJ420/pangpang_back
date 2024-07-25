package com.example.pangpang.controller.formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;


/*
 * 날짜/시간은 브라우저에서는 문자열로 전송되지만
 * 서버에서는 LocalDate 또는 LocalDateTime으로 처리되기 때문에
 * Controller에 formatter패키지를 만들어서 이를 변환해주는 Formatter를 만드는것
 */
public class LocalDateFomatter implements Formatter<LocalDate>{

    @Override
    public LocalDate parse(String text, Locale locale) {
        
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(object);
    }
    
}
