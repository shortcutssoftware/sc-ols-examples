package com.shortcuts.example.giftcard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortcuts.example.giftcard.Models.RequestBody.ActivateCardRequestBody;
import com.shortcuts.example.giftcard.Models.RequestBody.CancelLastRequestBody;
import com.shortcuts.example.giftcard.Models.RequestBody.RedeemCardRequestBody;
import com.shortcuts.example.giftcard.Models.RequestBody.ReloadCardRequestBody;
import com.shortcuts.example.giftcard.Models.RequestHeader;
import com.shortcuts.example.giftcard.Models.Response.CardServiceResponse;
import com.shortcuts.example.giftcard.Models.Response.TransactionResponse;
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

    /**
     * Activate GiftCard
     * Endpoint: /giftcard/{giftcard_number}/activate
     *
     * @param giftcardNumber @required
     * @param requestHeader  @required
     * @param requestBody @required
     * @return TransactionResponse
     */
    @SneakyThrows
    public TransactionResponse activateGiftCard(String giftcardNumber, RequestHeader requestHeader,
                                                ActivateCardRequestBody requestBody){
        String endpoint = String.format(baseUrl, giftcardNumber) + "/activate";
        HttpPost httpPost = setupHttpPost(endpoint, requestHeader);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        String httpResponseBody = sendPostRequest(httpPost);

        TransactionResponse response = objectMapper.readValue(httpResponseBody, TransactionResponse.class);
        return response;
    }

    /**
     * Balance inquire
     * Endpoint: /giftcard/{giftcard_number}
     *
     * @param giftcardNumber @required
     * @param requestHeader @required
     * @return CardServiceResponse
     */
    @SneakyThrows
    public CardServiceResponse balanceInquire(String giftcardNumber, RequestHeader requestHeader){
        String endpoint = String.format(baseUrl, giftcardNumber);
        HttpGet httpGet = setupHttpGet(endpoint, requestHeader);
        String httpResponseBody = sendGetRequest(httpGet);

        CardServiceResponse response = objectMapper.readValue(httpResponseBody, CardServiceResponse.class);
        return response;
    }

    /**
     * Redeem giftcard
     * Endpoint: /giftcard/{giftcard_number}/redeem
     *
     * @param giftcardNumber @required
     * @param requestHeader @required
     * @param requestBody @required
     * @return CardServiceResponse
     */
    @SneakyThrows
    public CardServiceResponse redeemGiftCard(String giftcardNumber, RequestHeader requestHeader,
                                              RedeemCardRequestBody requestBody){
        String endpoint = String.format(baseUrl, giftcardNumber) + "/redeem";
        HttpPost httpPost = setupHttpPost(endpoint, requestHeader);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        String httpResponseBody = sendPostRequest(httpPost);

        CardServiceResponse response = objectMapper.readValue(httpResponseBody, CardServiceResponse.class);
        return response;
    }

    /**
     * Reload GiftCard
     * Endpoint: /giftcard/{giftcard_number}/reload
     *
     * @param giftcardNumber @required
     * @param requestHeader @required
     * @param requestBody @required
     * @return TransactionResponse
     */
    @SneakyThrows
    public TransactionResponse reloadGiftCard(
            String giftcardNumber,
            RequestHeader requestHeader,
            ReloadCardRequestBody requestBody
    ){
        String endpoint = String.format(baseUrl, giftcardNumber) + "/reload";
        HttpPost httpPost = setupHttpPost(endpoint, requestHeader);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        String httpResponseBody = sendPostRequest(httpPost);

        TransactionResponse response = objectMapper.readValue(httpResponseBody, TransactionResponse.class);
        return response;
    }

    /**
     * Cancel last operation
     * Endpoint: giftcard/{giftcard_number}/cancel_last_operation
     *
     * @param giftcardNumber @required
     * @param requestHeader @required
     * @param requestBody @required
     * @return CardServiceResponse
     */
    @SneakyThrows
    public CardServiceResponse cancelLastOperation(
            String giftcardNumber,
            RequestHeader requestHeader,
            CancelLastRequestBody requestBody
    ){
        String endpoint = String.format(baseUrl, giftcardNumber) + "/cancel_last_operation";
        HttpPost httpPost = setupHttpPost(endpoint, requestHeader);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        String httpResponseBody = sendPostRequest(httpPost);

        CardServiceResponse response = objectMapper.readValue(httpResponseBody, CardServiceResponse.class);
        return response;
    }

    private ObjectMapper configureObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    private HttpGet setupHttpGet(String endpoint, RequestHeader requestHeader){
        HttpGet httpGet = new HttpGet(endpoint);
        httpGet.addHeader("Authorization", String.format("JWT %s", requestHeader.getJwtToken()));
        httpGet.addHeader("username", requestHeader.getSerialNumber());
        return httpGet;
    }

    private HttpPost setupHttpPost(String endpoint, RequestHeader requestHeader){
        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.addHeader("Authorization", String.format("JWT %s", requestHeader.getJwtToken()));
        httpPost.addHeader("username", requestHeader.getSerialNumber());
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
