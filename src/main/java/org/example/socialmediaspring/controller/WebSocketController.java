package org.example.socialmediaspring.controller;

import org.example.socialmediaspring.dto.chat.Message;
import org.example.socialmediaspring.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class WebSocketController {
    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    @Autowired
    private WebSocketService service;

    @MessageMapping("/books")
    @SendTo("/topic/book")
    public String sendMessage(@RequestBody final Message message) {
        log.info("Start send message ");

        return "Borrow books success!";
    }
}
