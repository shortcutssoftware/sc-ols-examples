package com.shortcuts.example.java;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
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

    public URI getEndpoint(String... pathSegments) {
        return getEndpoint(Optional.empty(), pathSegments);
    }

    public URI getEndpoint(Optional<MultiValueMap<String, String>> queryParameters, String... pathSegments) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);
        String[] segments = pathSegments != null ? pathSegments : new String[]{};
        String path = String.format("/%s", String.join("/", segments));
        uriComponentsBuilder.path(path);
        queryParameters.ifPresent(uriComponentsBuilder::queryParams);
        return uriComponentsBuilder.build().toUri();
    }
}
