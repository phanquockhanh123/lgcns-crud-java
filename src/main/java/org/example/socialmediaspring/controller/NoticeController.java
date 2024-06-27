package org.example.socialmediaspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/websocket")
@Slf4j
public class NoticeController {
    public static final String HELLO_TEXT = "Hello from Spring Boot Backend!";
    public static final String SECURED_TEXT = "Hello from the secured resource!";

    @ResponseBody
    @RequestMapping(path = "/hello")
    public String sayHello() {
        return HELLO_TEXT;
    }
}
