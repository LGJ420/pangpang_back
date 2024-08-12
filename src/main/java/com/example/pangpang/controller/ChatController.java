package com.example.pangpang.controller;

import com.example.pangpang.dto.ChatRoomDTO;
import com.example.pangpang.service.ChatService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {


    private final ChatService service;

    @PostMapping
    public ChatRoomDTO createRoom(@RequestParam String name){
        return service.createRoom(name);
    }

    @GetMapping
    public List<ChatRoomDTO> findAllRooms(){
        return service.findAllRoom();
    }

}
