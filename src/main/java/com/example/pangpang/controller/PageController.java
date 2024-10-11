package com.example.pangpang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    
    @GetMapping
    public String redirect(){

        return "redirect:https://pangpang-games.netlify.app";
    }
}
