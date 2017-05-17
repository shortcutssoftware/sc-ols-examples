package com.shortcuts.example.single_signon.salon;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * This is an example authenticator, it represents the customer authentication strategy.
 * In its current form it will issue a token for any credentials. This will be different
 * in a real system, however, the important mechanisms are that that the class must
 * <p>
 * 1. validate credentials.
 * 2. register the token so that it can be validated at a later time.
 * 3. return the token so that it can be passed on to Shortcuts.
 * </p>
 */
@RestController
public class SalonAuthorizationServer {

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    private ResponseEntity<String> authenticate(RequestEntity<String> request) throws IOException {

        try {

            // convert the input into a authenticationCredentials object
            String requestBody = request.getBody();
            AuthenticationCredentials authenticationCredentials = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(requestBody, AuthenticationCredentials.class);

            // validate the supplied authenticationCredentials
            ValidatedCredentials validatedCredentials = validate(authenticationCredentials);

            // register the token for validation later
            String token = authenticationTokenService.registerValidCredentials(validatedCredentials);

            // return the token to be passed to Shortcuts.
            return new ResponseEntity<>(token, HttpStatus.OK);

        } catch (Exception e) {

            // authentication credentials were not valid
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }

    }

    private ValidatedCredentials validate(AuthenticationCredentials authenticationCredentials) {

        if (StringUtils.isEmpty(authenticationCredentials.getEmail())) {
            throw new IllegalArgumentException("missing email in authenticationCredentials");
        }

        if (StringUtils.isEmpty(authenticationCredentials.getFirstName())) {
            throw new IllegalArgumentException("missing first_name in authenticationCredentials");
        }

        if (StringUtils.isEmpty(authenticationCredentials.getLastName())) {
            throw new IllegalArgumentException("missing last_name in authenticationCredentials");
        }

        // real implementations will be in interested in the
        // password, or any other means of real authentication

        // authenticationCredentials are valid
        ValidatedCredentials validatedCredentials = new ValidatedCredentials();
        validatedCredentials.setFirstName(authenticationCredentials.getFirstName());
        validatedCredentials.setLastName(authenticationCredentials.getLastName());
        validatedCredentials.setEmail(authenticationCredentials.getEmail());
        return validatedCredentials;
    }

}
