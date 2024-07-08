package org.example.socialmediaspring.config;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.props.MomoApiConfigProps;
import org.example.socialmediaspring.service.AccessTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MomoApiConfig {
    private final AccessTokenProvider provider;
    private final MomoApiConfigProps props;

    @Bean
    RequestInterceptor requestInterceptor(){
        return requestTemplate -> requestTemplate
                .header("Authorization", "Bearer "+ provider.getAccessToken())
                .header("X-Target-Environment", props.getTargetEnvironment())
                .header("Ocp-Apim-Subscription-Key", props.getOcpApimSubscriptionKey());
    }
}
