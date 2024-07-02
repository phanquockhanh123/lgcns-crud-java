package org.example.socialmediaspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.chat.ResponseMessage;
import org.example.socialmediaspring.dto.chat.Message;
import org.example.socialmediaspring.service.NoticeService;
import org.example.socialmediaspring.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

@RestController
@RequestMapping("/websocket")
@Slf4j
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(final Message message) throws InterruptedException {
        Thread.sleep(1000);
        noticeService.sendGlobalNotification();
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }
}
