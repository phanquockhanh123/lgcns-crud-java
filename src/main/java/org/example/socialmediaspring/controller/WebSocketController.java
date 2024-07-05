package org.example.socialmediaspring.controller;

import org.example.socialmediaspring.dto.chat.Message;
import org.example.socialmediaspring.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
public class WebSocketController {

    @Autowired
    private WebSocketService service;

    @MessageMapping("/books")
    @SendTo("/topic/book")
    public Message sendMessage(final Message message) throws Exception {
        return message;
    }
}
