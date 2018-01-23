package com.shortcuts.example.java.services;

import com.shortcuts.example.java.BaseUrlAware;
import com.shortcuts.example.java.util.RestTemplateCallingUtil;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Arrays;

public abstract class BaseShortcutsAPIService<T, R>
        extends BaseUrlAware
        implements ShortcutsAPIService<T, R> {

    @Autowired
    protected RestTemplateCallingUtil restTemplateCallingUtil;

    private final HttpMethod httpMethod;

    public BaseShortcutsAPIService(@NonNull HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    protected HttpHeaders setupAuthorizationHeader(HttpHeaders httpHeaders, String jwtToken) {
        String authorizationHeader = String.format("JWT %s", jwtToken);
        HttpHeaders result = httpHeaders;
        if (result == null) {
            result = new HttpHeaders();
        }
        if (!result.containsKey(HttpHeaders.AUTHORIZATION)) {
            result.put(HttpHeaders.AUTHORIZATION, Arrays.asList(authorizationHeader));
        }
        return result;
    }
}
