package com.shortcuts.example.java.services;

import com.shortcuts.example.java.BaseUrlAware;
import com.shortcuts.example.java.util.RestTemplateCallingUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public abstract class BaseShortcutsAPIService extends BaseUrlAware {

    @Autowired
    protected RestTemplateCallingUtil restTemplateCallingUtil;

    protected final HttpMethod httpMethod;

    public BaseShortcutsAPIService(@NonNull HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    protected HttpHeaders setupAuthorizationHeader(Optional<HttpHeaders> httpHeaders, String jwtToken) {
        String authorizationHeader = String.format("JWT %s", jwtToken);
        HttpHeaders result = httpHeaders.isPresent() ? httpHeaders.get() : new HttpHeaders();
        if (!result.containsKey(HttpHeaders.AUTHORIZATION)) {
            result.put(HttpHeaders.AUTHORIZATION, Arrays.asList(authorizationHeader));
        } else {
            log.warn(
                    "[{}] header was already set to [{}]",
                    HttpHeaders.AUTHORIZATION,
                    result.get(HttpHeaders.AUTHORIZATION));
        }
        return result;
    }
}
