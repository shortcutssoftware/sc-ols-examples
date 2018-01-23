package com.shortcuts.example.java.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

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
            String uri,
            HttpHeaders headers,
            Class<T> clazz) {
        HttpMethod method = HttpMethod.GET;
        HttpEntity<String> httpEntity = getRequestEntity(headers, null);
        return getResponseObject(
                uri,
                method,
                httpEntity,
                clazz);
    }

    public <T> T postForObject(
            String uri,
            HttpHeaders headers,
            Object requestBody,
            Class<T> clazz) {
        HttpMethod method = HttpMethod.POST;
        HttpEntity<String> httpEntity = getRequestEntity(headers, requestBody);
        return getResponseObject(
                uri,
                method,
                httpEntity,
                clazz);
    }

    private <T> T getResponseObject(
            String uri,
            HttpMethod method,
            HttpEntity<String> requestEntity,
            Class<T> clazz) {
        try {
            log.info("request {} {}, entity: {}", method, uri, requestEntity);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    uri,
                    method,
                    requestEntity,
                    String.class);
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