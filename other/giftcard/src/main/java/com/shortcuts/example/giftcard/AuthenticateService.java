package com.shortcuts.example.giftcard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortcuts.example.giftcard.Models.RequestBody.AuthenticateRequestBody;
import com.shortcuts.example.giftcard.Models.Response.AuthenticateResponse;
import com.shortcuts.example.giftcard.Models.Response.BaseResponse;
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
    public AuthenticateResponse authenticate(AuthenticateRequestBody authenticateRequestBody){
        HttpPost httpPost = new HttpPost(authenticateUrl);

        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(authenticateRequestBody)));
        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());

        if(httpResponse.getStatusLine().getStatusCode() != 200){
            BaseResponse baseResponse = objectMapper.readValue(jsonResponse, BaseResponse.class);
            throw new RuntimeException(String.format("Error Type: %s, %s", baseResponse.getError_type_code(), baseResponse.getMessage()));
        }

        AuthenticateResponse authenticateResponse = objectMapper.readValue(jsonResponse, AuthenticateResponse.class);
        return authenticateResponse;
    }
}
