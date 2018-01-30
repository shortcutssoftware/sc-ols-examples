package com.shortcuts.example.java.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

/**
 * Some convenience methods for various ForObject methods with headers.
 */
@Slf4j
@Component
public class RestTemplateCallingUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T getForObject(
            URI uri,
            HttpHeaders headers,
            Class<T> clazz,
            Object... uriVariables) {
        HttpMethod method = HttpMethod.GET;
        HttpEntity<String> httpEntity = getRequestEntity(headers, null);
        return getResponseObject(
                uri,
                method,
                httpEntity,
                clazz,
                uriVariables);
    }

    public <T> T postForObject(
            URI uri,
            HttpHeaders headers,
            Object requestBody,
            Class<T> clazz,
            Object... uriVariables) {
        HttpMethod method = HttpMethod.POST;
        HttpEntity<String> httpEntity = getRequestEntity(headers, requestBody);
        return getResponseObject(
                uri,
                method,
                httpEntity,
                clazz,
                uriVariables);
    }

    private <T> T getResponseObject(
            URI uri,
            HttpMethod method,
            HttpEntity<String> requestEntity,
            Class<T> clazz,
            Object... uriVariables) {
        try {
            log.info("request {} {}, entity: {}", method, uri, requestEntity);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    uri.toString(),
                    method,
                    requestEntity,
                    String.class,
                    uriVariables);
            HttpStatus responseEntityStatusCode = responseEntity.getStatusCode();
            String responseEntityBody = responseEntity.getBody();
            log.info("response status code [{}], body: {}", responseEntityStatusCode, responseEntityBody);
            return objectMapper.readValue(responseEntityBody, clazz);
        } catch (IOException e) {
            throw new ShortcutsAPIException(e);
        }
    }

    private HttpEntity<String> getRequestEntity(HttpHeaders headers, Object requestBody) {
        HttpHeaders httpHeaders = headers != null ? headers : new HttpHeaders();
        if (requestBody != null) {
            return new HttpEntity<>(getRequestBodyAsString(requestBody), httpHeaders);
        } else {
            return new HttpEntity<>(httpHeaders);
        }
    }

    private String getRequestBodyAsString(Object requestBody) {
        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new ShortcutsAPIException(e);
        }
    }
}