package org.example.socialmediaspring.clients;

import org.example.socialmediaspring.config.MomoApiConfig;
import org.example.socialmediaspring.dto.momo.AccountBalanceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "open-api-client",
        url = "https://sandbox.momodeveloper.mtn.com/remittance",
        configuration = MomoApiConfig.class

)
public interface MomoApiClients {
    @GetMapping("/v1_0/account/balance")
    AccountBalanceDto getAccountBalance();
}
