package com.shortcuts.example.java.services;

import com.shortcuts.example.java.util.ShortcutsAPIException;
import org.springframework.http.HttpHeaders;

public interface ShortcutsAPIService<T, R> {

    /**
     * Call the API using the supplied JWT token and request object.
     *
     * @param jwtToken
     * @param requestObject
     * @return response object
     * @throws ShortcutsAPIException
     */
    default R call(
            String jwtToken,
            T requestObject) throws ShortcutsAPIException {
        return call(
                jwtToken,
                new HttpHeaders(),
                requestObject);
    }

    /**
     * Call the API using the supplied JWT token, http headers and request object.
     *
     * @param jwtToken
     * @param httpHeaders
     * @param requestObject
     * @return response object
     * @throws ShortcutsAPIException
     */
    R call(
            String jwtToken,
            HttpHeaders httpHeaders,
            T requestObject) throws ShortcutsAPIException;

}
