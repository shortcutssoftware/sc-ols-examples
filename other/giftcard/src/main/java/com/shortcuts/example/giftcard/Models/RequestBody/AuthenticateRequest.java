package com.shortcuts.example.giftcard.Models.RequestBody;

import lombok.Data;

@Data
public class AuthenticateRequest {
    private String credential_type_code;
}
