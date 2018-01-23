package com.shortcuts.example.java.authentication;

import org.springframework.web.client.HttpStatusCodeException;

public interface JWTAuthentication {

    /**
     * @return String containing the JWT token that resulted from an authentication attempt.
     */
    String authenticate() throws HttpStatusCodeException;
}
