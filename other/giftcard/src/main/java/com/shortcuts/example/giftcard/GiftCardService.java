package com.shortcuts.example.giftcard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shortcuts.example.giftcard.Models.RequestBody.ActivateCardRequestBody;
import com.shortcuts.example.giftcard.Models.RequestBody.CancelLastRequestBody;
import com.shortcuts.example.giftcard.Models.RequestBody.RedeemCardRequestBody;
import com.shortcuts.example.giftcard.Models.RequestBody.ReloadCardRequestBody;
import com.shortcuts.example.giftcard.Models.RequestHeaders;
import com.shortcuts.example.giftcard.Models.Response.BaseResponse;
import com.shortcuts.example.giftcard.Models.Response.CardServiceResponse;
import com.shortcuts.example.giftcard.Models.Response.TransactionResponse;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class GiftCardService {

    String baseUrl = "https://api.shortcutssoftware.io/giftcard/%s";

    ObjectMapper objectMapper = configureObjectMapper();

    /**
     * Activate GiftCard
     * Endpoint: /giftcard/{giftcard_number}/activate
     *
     * @param giftcardNumber @required
     * @param requestHeaders @required
     * @param requestBody    @required
     * @return TransactionResponse
     */
    @SneakyThrows
    public TransactionResponse activateGiftCard(String giftcardNumber, RequestHeaders requestHeaders,
                                                ActivateCardRequestBody requestBody) {
        String endpoint = String.format(baseUrl, giftcardNumber) + "/activate";
        HttpPost httpPost = setupHttpPost(endpoint, requestHeaders);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        TransactionResponse response = sendHttpRequest(httpPost, TransactionResponse.class);
        return response;
    }

    /**
     * Balance inquire
     * Endpoint: /giftcard/{giftcard_number}
     *
     * @param giftcardNumber @required
     * @param requestHeaders @required
     * @return CardServiceResponse
     */
    @SneakyThrows
    public CardServiceResponse balanceInquire(String giftcardNumber, RequestHeaders requestHeaders) {
        String endpoint = String.format(baseUrl, giftcardNumber);
        HttpGet httpGet = setupHttpGet(endpoint, requestHeaders);

        CardServiceResponse response = sendHttpRequest(httpGet, CardServiceResponse.class);
        return response;
    }

    /**
     * Redeem giftcard
     * Endpoint: /giftcard/{giftcard_number}/redeem
     *
     * @param giftcardNumber @required
     * @param requestHeaders @required
     * @param requestBody    @required
     * @return CardServiceResponse
     */
    @SneakyThrows
    public CardServiceResponse redeemGiftCard(String giftcardNumber, RequestHeaders requestHeaders,
                                              RedeemCardRequestBody requestBody) {
        String endpoint = String.format(baseUrl, giftcardNumber) + "/redeem";
        HttpPost httpPost = setupHttpPost(endpoint, requestHeaders);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        CardServiceResponse response = sendHttpRequest(httpPost, CardServiceResponse.class);

        return response;
    }

    /**
     * Reload GiftCard
     * Endpoint: /giftcard/{giftcard_number}/reload
     *
     * @param giftcardNumber @required
     * @param requestHeaders @required
     * @param requestBody    @required
     * @return TransactionResponse
     */
    @SneakyThrows
    public TransactionResponse reloadGiftCard(
            String giftcardNumber,
            RequestHeaders requestHeaders,
            ReloadCardRequestBody requestBody
    ) {
        String endpoint = String.format(baseUrl, giftcardNumber) + "/reload";
        HttpPost httpPost = setupHttpPost(endpoint, requestHeaders);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        TransactionResponse response = sendHttpRequest(httpPost, TransactionResponse.class);

        return response;
    }

    /**
     * Cancel last operation
     * Endpoint: giftcard/{giftcard_number}/cancel_last_operation
     *
     * @param giftcardNumber @required
     * @param requestHeaders @required
     * @param requestBody    @required
     * @return CardServiceResponse
     */
    @SneakyThrows
    public CardServiceResponse cancelLastOperation(
            String giftcardNumber,
            RequestHeaders requestHeaders,
            CancelLastRequestBody requestBody
    ) {
        String endpoint = String.format(baseUrl, giftcardNumber) + "/cancel_last_operation";
        HttpPost httpPost = setupHttpPost(endpoint, requestHeaders);
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBody)));
        CardServiceResponse response = sendHttpRequest(httpPost, CardServiceResponse.class);

        return response;
    }

    private ObjectMapper configureObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    private HttpGet setupHttpGet(String endpoint, RequestHeaders requestHeaders) {
        HttpGet httpGet = new HttpGet(endpoint);
        httpGet.addHeader("Authorization", String.format("JWT %s", requestHeaders.getJwtToken()));
        httpGet.addHeader("username", requestHeaders.getSerialNumber());
        return httpGet;
    }

    private HttpPost setupHttpPost(String endpoint, RequestHeaders requestHeaders) {
        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        httpPost.addHeader("Authorization", String.format("JWT %s", requestHeaders.getJwtToken()));
        httpPost.addHeader("username", requestHeaders.getSerialNumber());
        return httpPost;
    }

    @SneakyThrows
    private <T extends BaseResponse> T sendHttpRequest(HttpUriRequest httpRequest, Class<T> clazz){
        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpRequest);

        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            BaseResponse baseResponse = objectMapper.readValue(jsonResponse, BaseResponse.class);
            throw new RuntimeException(String.format("Error Type: %s, %s", baseResponse.getError_type_code(), baseResponse.getMessage()));
        }
        return objectMapper.readValue(jsonResponse, clazz);
    }
}