package org.example.socialmediaspring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.clients.MomoApiClients;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties
@RequiredArgsConstructor
@Slf4j
public class SocialMediaSpringApplication {
    private final MomoApiClients momoApiClient;

//    @Bean
//    ApplicationRunner application(){
//        return args -> {
//            var response = momoApiClient.getAccountBalance();
//            log.info("Acccount Balance from MomoApi: {}", response);
//
//        };
//    }


    public static void main(String[] args) {
        SpringApplication.run(SocialMediaSpringApplication.class, args);
    }

}
