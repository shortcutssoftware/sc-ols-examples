package com.shortcuts.example.giftcard.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestHeaders {

    private String jwtToken;

    private String serialNumber;
}
