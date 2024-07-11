package org.example.socialmediaspring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.clients.MomoApiAuthClient;
import org.example.socialmediaspring.props.MomoApiConfigProps;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessTokenProvider {
    private final MomoApiAuthClient authClient;
    private final MomoApiConfigProps props;

    public String getAccessToken(){
        String header = Base64.getEncoder().encodeToString(String.format("%s:%s", props.getApiUserId(), props.getApiKey()).getBytes()
        );
        log.info("Header " + header);
        var authResponse = authClient.getAccessToken("Basic "+header, props.getOcpApimSubscriptionKey());

        log.info("access token: " + authResponse.accessToken());
        return authResponse.accessToken();

    }
}
