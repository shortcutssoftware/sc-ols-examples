package com.shortcuts.example.giftcard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortcuts.example.giftcard.Models.RequestBody.AuthenticateRequest;
import com.shortcuts.example.giftcard.Models.RequestBody.AuthenticateRequestHeaders;
import com.shortcuts.example.giftcard.Models.Response.AuthenticateResponse;
import com.shortcuts.example.giftcard.Models.Response.BaseResponse;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.client.CoreOAuthConsumerSupport;
import org.springframework.security.oauth.provider.BaseConsumerDetails;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class AuthenticateService {

    String authenticateUrl = "https://api.shortcutssoftware.io/authenticate";

    ObjectMapper objectMapper = new ObjectMapper();

    private AuthenticateRequestHeaders requestHeaders;

    public AuthenticateService(AuthenticateRequestHeaders requestHeaders){
        this.requestHeaders = requestHeaders;
    }

    @SneakyThrows
    public AuthenticateResponse authenticate(){
        AuthenticateRequest requestBody = new AuthenticateRequest();
        requestBody.setCredential_type_code("oauth");

        HttpPost httpPost = new HttpPost(authenticateUrl);
        httpPost.addHeader("Authorization", getSignedAuthorizationHeader());
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));

        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());

        if(httpResponse.getStatusLine().getStatusCode() != 200){
            BaseResponse baseResponse = objectMapper.readValue(jsonResponse, BaseResponse.class);
            throw new RuntimeException(String.format("Error Type: %s, %s", baseResponse.getError_type_code(), baseResponse.getMessage()));
        }

        AuthenticateResponse authenticateResponse = objectMapper.readValue(jsonResponse, AuthenticateResponse.class);
        return authenticateResponse;
    }

    /**
     * sign request with oauth credentials. we are using
     * spring-security-oauth here, but you can use any
     * method that complies with the standard.
     */
    private String getSignedAuthorizationHeader() {

        BaseConsumerDetails baseConsumerDetails = new BaseConsumerDetails();
        baseConsumerDetails.setConsumerKey(requestHeaders.getConsumerKey());
        baseConsumerDetails.setSignatureSecret(new SharedConsumerSecretImpl(requestHeaders.getConsumerSecret()));

        BaseProtectedResourceDetails resource = new BaseProtectedResourceDetails();
        resource.setId("GiftCardExample");
        resource.setConsumerKey(baseConsumerDetails.getConsumerKey());
        resource.setSharedSecret(baseConsumerDetails.getSignatureSecret());

        OAuthConsumerToken oAuthConsumerToken = new OAuthConsumerToken();
        oAuthConsumerToken.setResourceId(resource.getId());
        oAuthConsumerToken.setAccessToken(true);
        oAuthConsumerToken.setValue(requestHeaders.getAccessKey());
        oAuthConsumerToken.setSecret(requestHeaders.getAccessSecret());

        URL endpointUrl;
        try {
            endpointUrl = new URL(authenticateUrl);
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
