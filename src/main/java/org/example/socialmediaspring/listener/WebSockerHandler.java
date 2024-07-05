package org.example.socialmediaspring.listener;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.notifications.BookBorrowedNotification;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSockerHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    public void sendNotification(BookBorrowedNotification notification) {
        for (WebSocketSession session : sessions) {
            try {
                log.info(String.valueOf(123));
                session.sendMessage(new TextMessage(
                        objectMapper.writeValueAsString(notification)
                ));
            } catch (IOException | java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // Parse the message from the client
//        Map<String, String> messageMap = objectMapper.readValue(message.getPayload(), new TypeReference<Map<String, String>>() {});
//        String action = messageMap.get("action");
//        Long bookId = Long.valueOf(messageMap.get("bookId"));
//
//        if ("borrow-book".equals(action)) {
//            // Send the notification to the client
//            BookBorrowedNotification notification = notificationService.sendBookBorrowedNotification(bookId);
//            sendNotificationToPublicChannel(notification);
//        }
//    }
}
