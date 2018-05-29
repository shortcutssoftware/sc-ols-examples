package com.shortcuts.example.giftcard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticateResponse {

    @JsonProperty("access_token")
    private String accessToken;
}
