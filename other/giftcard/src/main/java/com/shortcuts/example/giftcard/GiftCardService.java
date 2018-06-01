package com.shortcuts.example.giftcard;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortcuts.example.giftcard.Models.RequestBody.ActivateCardRequestBody;
import com.shortcuts.example.giftcard.Models.RequestBody.CancelLastRequestBody;
import com.shortcuts.example.giftcard.Models.RequestBody.RedeemCardRequestBody;
import com.shortcuts.example.giftcard.Models.RequestBody.ReloadCardRequestBody;
import com.shortcuts.example.giftcard.Models.Response.CardServiceResponse;
import com.shortcuts.example.giftcard.Models.Response.TransactionServiceResponse;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class GiftCardService {

    //TODO: remove header password

    String baseUrl = "https://api.shortcutssoftware.io/giftcard/%s";

    ObjectMapper objectMapper = configureObjectMapper();

    @SneakyThrows
    public TransactionServiceResponse activateGiftCard(
            String giftcardNumber,
            String jwtToken,
            String serialNumber,
            String password,
            ActivateCardRequestBody requestBody
    ){
        //https://api.shortcutssoftware.io/giftcard/{giftcard_number}/activate
        String endpoint = String.format(baseUrl, giftcardNumber) + "/activate";
        HttpPost httpPost = setupHttpPost(endpoint, jwtToken, serialNumber, password);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        String httpResponseBody = sendPostRequest(httpPost);

        TransactionServiceResponse response = objectMapper.readValue(httpResponseBody, TransactionServiceResponse.class);
        return response;
    }

    @SneakyThrows
    public CardServiceResponse balanceInquire(
            String giftcardNumber,
            String jwtToken,
            String serialNumber,
            String password){
        //https://api.shortcutssoftware.io/giftcard/{giftcard_number}
        String endpoint = String.format(baseUrl, giftcardNumber);
        HttpGet httpGet = setupHttpGet(endpoint, jwtToken, serialNumber, password);
        String httpResponseBody = sendGetRequest(httpGet);

        CardServiceResponse response = objectMapper.readValue(httpResponseBody, CardServiceResponse.class);
        return response;
    }

    @SneakyThrows
    public CardServiceResponse RedeemGiftCard(
            String giftcardNumber,
            String jwtToken,
            String serialNumber,
            String password,
            RedeemCardRequestBody requestBody
    ){
        //https://api.shortcutssoftware.io/giftcard/{giftcard_number}/redeem
        String endpoint = String.format(baseUrl, giftcardNumber) + "/redeem";
        HttpPost httpPost = setupHttpPost(endpoint, jwtToken, serialNumber, password);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        String httpResponseBody = sendPostRequest(httpPost);

        CardServiceResponse response = objectMapper.readValue(httpResponseBody, CardServiceResponse.class);
        return response;
    }

    @SneakyThrows
    public TransactionServiceResponse ReloadGiftCard(
            String giftcardNumber,
            String jwtToken,
            String serialNumber,
            String password,
            ReloadCardRequestBody requestBody
    ){
        //https://api.shortcutssoftware.io/giftcard/{giftcard_number}/reload
        String endpoint = String.format(baseUrl, giftcardNumber) + "/reload";
        HttpPost httpPost = setupHttpPost(endpoint, jwtToken, serialNumber, password);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        String httpResponseBody = sendPostRequest(httpPost);

        TransactionServiceResponse response = objectMapper.readValue(httpResponseBody, TransactionServiceResponse.class);
        return response;
    }

    @SneakyThrows
    public CardServiceResponse CancelLastOperation(
            String giftcardNumber,
            String jwtToken,
            String serialNumber,
            String password,
            CancelLastRequestBody requestBody
    ){
        //https://api.shortcutssoftware.io/giftcard/{giftcard_number}/cancel_last_operation
        String endpoint = String.format(baseUrl, giftcardNumber) + "/cancel_last_operation";
        HttpPost httpPost = setupHttpPost(endpoint, jwtToken, serialNumber, password);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        String httpResponseBody = sendPostRequest(httpPost);

        CardServiceResponse response = objectMapper.readValue(httpResponseBody, CardServiceResponse.class);
        return response;
    }

    private ObjectMapper configureObjectMapper(){
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private HttpGet setupHttpGet(String endpoint, String jwtToken, String serialNumber, String password){
        HttpGet httpGet = new HttpGet(endpoint);
        httpGet.addHeader("Authorization", String.format("JWT %s", jwtToken));
        httpGet.addHeader("username", serialNumber);
        httpGet.addHeader("password", password);
        return httpGet;
    }

    private HttpPost setupHttpPost(String endpoint, String jwtToken, String serialNumber, String password){
        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.addHeader("Authorization", String.format("JWT %s", jwtToken));
        httpPost.addHeader("username", serialNumber);
        httpPost.addHeader("password", password);
        return httpPost;
    }

    @SneakyThrows
    private String sendGetRequest(HttpGet httpGet){
        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpGet);
        return EntityUtils.toString(httpResponse.getEntity());
    }

    @SneakyThrows
    private String sendPostRequest(HttpPost httpPost){
        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);
        return EntityUtils.toString(httpResponse.getEntity());
    }
}
