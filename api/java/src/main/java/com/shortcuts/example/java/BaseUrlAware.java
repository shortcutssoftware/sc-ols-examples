package com.shortcuts.example.java;

import org.springframework.beans.factory.annotation.Value;

public class BaseUrlAware {

    @Value("${baseUrl}")
    private String baseUrl;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getEndpoint(String... pathSegments) {
        String[] segments = pathSegments != null ? pathSegments : new String[] {};
        return String.format("%s/%s", baseUrl, String.join("/", segments));
    }

}
