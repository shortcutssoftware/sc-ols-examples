package com.shortcuts.example.java;

import org.springframework.beans.factory.annotation.Value;

public class BaseUrlAware {

    @Value("${baseUrl}")
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }
}
