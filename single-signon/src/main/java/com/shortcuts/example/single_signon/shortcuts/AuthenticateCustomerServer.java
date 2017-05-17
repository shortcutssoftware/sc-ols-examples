package com.shortcuts.example.single_signon.shortcuts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This is an example Shortcuts server which provides an endpoint
 * for salons to authenticate customers.
 */
@RestController
public class AuthenticateCustomerServer {

    @RequestMapping(value = "/authenticate_customer", method = RequestMethod.POST)
    private ResponseEntity<String> authenticate(RequestEntity<String> request) throws IOException {

        try {

            // get the token from the Authorization header
            String authorizationHeader = request.getHeaders().get("Authorization").get(0);

            if (validateOAuthToken(authorizationHeader)) {

                // Salon credentials are good

                // deserialize request body
                String body = request.getBody();
                AuthenticateCustomerBody authenticateCustomerBody = new ObjectMapper().readValue(body, AuthenticateCustomerBody.class);

                if (validateCustomerToken(authenticateCustomerBody)) {
                    // customer token is good
                    return new ResponseEntity<>("in real life this body would contain a Shortcuts customer session response", HttpStatus.OK);
                }

            }

        } catch (Exception e) {
            // unauthorized, fall through
        }

        // authentication failed, return error
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Naive example of OAuth validation
     *
     * @param authorizationHeader
     * @return a boolean indicating if teh OAuth credentials were valid.
     */
    private boolean validateOAuthToken(String authorizationHeader) {
        return !StringUtils.isEmpty(authorizationHeader);
    }

    /**
     * An example implementation of customer token validation
     * using a callback to the Salon Resource Server.
     *
     * @param authenticateCustomerBody
     * @return a boolean indicating whether the Salon Resource Server validated the customer token.
     */
    private boolean validateCustomerToken(AuthenticateCustomerBody authenticateCustomerBody) throws Exception {

        URI callbackUri = getCallbackUri(authenticateCustomerBody.getTokenType());

        // prepare the callback request to the Salon Resource Server
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s", authenticateCustomerBody.getAccessToken()));
        RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, callbackUri);

        // make the HTTP request to the Salon Resource Server
        try {
            ResponseEntity<String> response = new RestTemplate().exchange(request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // validation of customer token succeeded
                return true;
            }
        } catch (Exception e) {
            // callback error, fall through
        }

        return false;
    }

    private URI getCallbackUri(String tokenType) throws URISyntaxException {
        return new URI("http://localhost:8080/validate");
    }
}
