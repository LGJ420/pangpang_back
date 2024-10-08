package com.example.pangpang.chat;

import com.example.pangpang.dto.ChatMessageDTO;
import com.example.pangpang.dto.ChatRoomDTO;
import com.example.pangpang.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;



/*
* WebSocket Handler 작성
* 소켓 통신은 서버와 클라이언트가 1:n으로 관계를 맺는다. 따라서 한 서버에 여러 클라이언트 접속 가능
* 서버에는 여러 클라이언트가 발송한 메세지를 받아 처리해줄 핸들러가 필요
* TextWebSocketHandler를 상속받아 핸들러 작성
* 클라이언트로 받은 메세지를 log로 출력하고 클라이언트로 환영 메세지를 보내줌
* */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    private final ChatService service;

    @SuppressWarnings("null")
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        ChatMessageDTO chatMessage = mapper.readValue(payload, ChatMessageDTO.class);
        log.info("session {}", chatMessage.toString());

        ChatRoomDTO room = service.findRoomById(chatMessage.getRoomId());
        log.info("room {}", room.toString());

        room.handleAction(session, chatMessage, service);
    }

}