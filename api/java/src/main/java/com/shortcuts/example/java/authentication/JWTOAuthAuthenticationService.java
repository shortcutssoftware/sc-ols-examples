package com.shortcuts.example.java.authentication;

import com.shortcuts.example.java.BaseUrlAware;
import com.shortcuts.example.java.util.RestTemplateCallingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.client.CoreOAuthConsumerSupport;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

@Slf4j
@Service
public class JWTOAuthAuthenticationService extends BaseUrlAware implements JWTAuthentication {

    @Autowired
    private RestTemplateCallingUtil restTemplateCallingUtil;

    @Value("${authentication.oauth.consumerKey}")
    private String consumerKey;

    @Value("${authentication.oauth.consumerSecret}")
    private String consumerSecret;

    @Value("${authentication.oauth.accessKey}")
    private String accessKey;

    @Value("${authentication.oauth.accessSecret}")
    private String accessSecret;

    public RestTemplateCallingUtil getRestTemplateCallingUtil() {
        return restTemplateCallingUtil;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    @Override
    public String authenticate() {

        // set up empty authentication request object with
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setCredential_type_code("oauth");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, getSignedAuthorizationHeader());

        // call the api
        AuthenticationResponse authenticationResponse = restTemplateCallingUtil.postForObject(
                getEndpoint("authenticate"),
                httpHeaders,
                authenticationRequest,
                AuthenticationResponse.class);

        String jwtToken = authenticationResponse.getAccess_token();
        log.info("authentication result: {}", jwtToken);

        return jwtToken;
    }

    /**
     * sign request with oauth credentials. we are using
     * spring-security-oauth here, but you can use any
     * method that complies with the standard.
     */
    private String getSignedAuthorizationHeader() {

        BaseConsumerDetails baseConsumerDetails = new BaseConsumerDetails();
        baseConsumerDetails.setConsumerKey(getConsumerKey());
        baseConsumerDetails.setSignatureSecret(new SharedConsumerSecretImpl(getConsumerSecret()));

        BaseProtectedResourceDetails resource = new BaseProtectedResourceDetails();
        resource.setId("MyExampleApplication");
        resource.setConsumerKey(baseConsumerDetails.getConsumerKey());
        resource.setSharedSecret(baseConsumerDetails.getSignatureSecret());

        OAuthConsumerToken oAuthConsumerToken = new OAuthConsumerToken();
        oAuthConsumerToken.setResourceId(resource.getId());
        oAuthConsumerToken.setAccessToken(true);
        oAuthConsumerToken.setValue(getAccessKey());
        oAuthConsumerToken.setSecret(getAccessSecret());

        URL endpointUrl;
        try {
            endpointUrl = getEndpoint("authenticate").toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return new CoreOAuthConsumerSupport().getAuthorizationHeader(
                resource,
                oAuthConsumerToken,
                endpointUrl,
                HttpMethod.POST.name(),
                Collections.emptyMap());
    }
}
