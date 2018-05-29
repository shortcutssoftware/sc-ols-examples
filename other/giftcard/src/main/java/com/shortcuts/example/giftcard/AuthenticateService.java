package com.shortcuts.example.giftcard;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class AuthenticateService {

    String authenticateUrl = "https://api.shortcutssoftware.io/authenticate";

    ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public AuthenticateResponse authenticate(AuthenticateRequest authenticateRequest){
        HttpPost httpPost = new HttpPost(authenticateUrl);

        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(authenticateRequest)));
        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
        String httpResponseBody = EntityUtils.toString(httpResponse.getEntity());

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if(statusCode == 200){
            AuthenticateResponse authenticateResponse = objectMapper.readValue(httpResponseBody, AuthenticateResponse.class);
            return authenticateResponse;
        }

        throw new RuntimeException(String.format("Authentication fail: %s, %s", statusCode, httpResponseBody));
    }
}
