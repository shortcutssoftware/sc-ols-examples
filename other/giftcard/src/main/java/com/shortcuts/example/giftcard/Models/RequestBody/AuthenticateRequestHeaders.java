package com.shortcuts.example.giftcard.Models.RequestBody;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticateRequestHeaders {
    private String consumerKey;
    private String consumerSecret;
    private String accessKey;
    private String accessSecret;
}
