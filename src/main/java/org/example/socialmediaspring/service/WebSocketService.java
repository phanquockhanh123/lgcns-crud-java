package org.example.socialmediaspring.service;

import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.chat.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NoticeService notificationService;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate, NoticeService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontend(final String message) {
        ResponseMessage response = new ResponseMessage(message);
        log.info(response.getContent());
        notificationService.sendGlobalNotification();

        messagingTemplate.convertAndSend("/topic/messages", response);
    }
}
