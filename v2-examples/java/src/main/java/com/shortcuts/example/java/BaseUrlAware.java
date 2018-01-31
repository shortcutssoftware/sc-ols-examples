package com.shortcuts.example.java;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

public class BaseUrlAware {

    @Value("${baseUrl}")
    private String baseUrl;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public URI getEndpointURI(String path, Object... uriVariables) {
        return getEndpointURI(path, Optional.empty(), uriVariables);
    }

    public URI getEndpointURI(
            String path,
            @NonNull Optional<MultiValueMap<String, String>> queryParameters,
            Object... uriVariables) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);
        if (path != null) {
            if (!path.startsWith("/")) {
                path = String.format("/%s", path);
            }
        } else {
            path = "/";
        }
        uriComponentsBuilder.path(path);
        queryParameters.ifPresent(uriComponentsBuilder::queryParams);
        UriComponents uriComponents;
        if (uriVariables != null) {
            uriComponents = uriComponentsBuilder.buildAndExpand(uriVariables);
        } else {
            uriComponents = uriComponentsBuilder.build();
        }
        return uriComponents.toUri();
    }
}
