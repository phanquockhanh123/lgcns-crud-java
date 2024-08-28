package org.example.socialmediaspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MessageSource messageSource;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello spring boot";
    }


    @GetMapping(path = "/hello-word-internationlized")
    public String convertLang() {
        Locale locale = LocaleContextHolder.getLocale();

        return  messageSource.getMessage("good.morning.message", null, "Default Message", locale);

    }

    //


}
