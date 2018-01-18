package com.shortcuts.example.single_signon.salon;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthenticationTokenService {

    private final Map<String, ValidatedCredentials> validatedCredentialTokens = new ConcurrentHashMap<>();

    /**
     * Register validated credentials so that they can be retrieved at a later time.
     *
     * @param validatedCredentials a set of validated credentials.
     * @return a token that can be used to retrieve these credentials.
     */
    public String registerValidCredentials(ValidatedCredentials validatedCredentials) {
        String authenticationToken = UUID.randomUUID().toString();
        validatedCredentialTokens.put(authenticationToken, validatedCredentials);
        return authenticationToken;
    }

    /**
     * Validate a token, return a set of validated credentials if the token is known.
     *
     * @param token a token to be validated.
     * @return validated credentials registered against the supplied token, or null if the token is not known.
     */
    public ValidatedCredentials validateAuthenticationToken(String token) {
        return validatedCredentialTokens.get(token);
    }

}
