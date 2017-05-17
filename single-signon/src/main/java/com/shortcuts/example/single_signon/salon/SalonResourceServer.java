package com.shortcuts.example.single_signon.salon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * This is an example token validator. It will attempt to retrieve validated credentials
 * for the supplied token. If they are found the validator will return them for use by
 * Shortcuts systems. If they are not found the validator will return an error.
 */
@RestController
public class SalonResourceServer {

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @RequestMapping(path = "/validate", method = RequestMethod.GET)
    private ResponseEntity<String> validate(RequestEntity<String> request) throws IOException {

        try {

            // get the token from the Authorization header
            String authorizationHeader = request.getHeaders().get("Authorization").get(0);

            // the authorization header is in the form of "Bearer <token>"
            String token = new ArrayDeque<>(Arrays.asList(authorizationHeader.split("\\s+"))).removeLast();

            // validate the token
            ValidatedCredentials validatedCredentials = authenticationTokenService.validateAuthenticationToken(token);

            if (validatedCredentials != null) {

                // token was valid, serialise and return the
                // validated credentials for use by Shortcuts
                String body = new ObjectMapper()
                        .configure(SerializationFeature.INDENT_OUTPUT, true)
                        .writeValueAsString(validatedCredentials);
                return new ResponseEntity<>(body, HttpStatus.OK);

            }

        } catch (Exception e) {

            // unauthorized, fall through

        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }


}
