package org.example.socialmediaspring.dto.momo;

import com.fasterxml.jackson.annotation.JsonProperty;

public record  AuthenticationResponseDto(
     @JsonProperty("access_token")
     String accessToken,
     @JsonProperty("token_type")
     String tokenType,
     @JsonProperty("expires_in")
     Integer expiresIn) {
}
