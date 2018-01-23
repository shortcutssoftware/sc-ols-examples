package com.shortcuts.example.java.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Some convenience methods for *ForObject methods with headers.
 */
@Component
public class RestTemplateCallingUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T postForObject(
            String uri,
            HttpHeaders headers,
            Object requestBody,
            Class<T> clazz) {
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(
                    objectMapper.writeValueAsString(requestBody),
                    headers != null ? headers : new HttpHeaders());
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    httpEntity,
                    clazz);
            return responseEntity.getBody();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}