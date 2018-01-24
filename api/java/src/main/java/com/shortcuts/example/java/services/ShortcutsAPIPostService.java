package com.shortcuts.example.java.services;

import com.shortcuts.example.java.util.ShortcutsAPIException;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

public interface ShortcutsAPIPostService<T, R> extends ShortcutsAPIService<T, R> {

    /**
     * Call the API using the supplied JWT token and request object.
     *
     * @param jwtToken
     * @param requestObject
     * @param httpHeaders
     * @param queryParameters
     * @return response object
     * @throws ShortcutsAPIException
     */
    R call(
            @NonNull String jwtToken,
            @NonNull T requestObject,
            @NonNull Optional<HttpHeaders> httpHeaders,
            @NonNull Optional<MultiValueMap<String, String>> queryParameters,
            Object... uriVariables) throws ShortcutsAPIException;

}
