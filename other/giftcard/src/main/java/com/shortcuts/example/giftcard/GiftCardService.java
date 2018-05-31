package com.shortcuts.example.giftcard;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class GiftCardService {

    String host = "https://api.shortcutssoftware.io";

    ObjectMapper objectMapper = new ObjectMapper();

    public void activateGiftCard(){

    }

    @SneakyThrows
    public void balanceInquire(String giftcardNumber, String jwtToken, String username, String password){
        String endpoint = String.format("%s/giftcard/%s", host, giftcardNumber);
        HttpGet httpGet = new HttpGet(endpoint);
        httpGet.addHeader("Authorization", String.format("JWT %s", jwtToken));
        httpGet.addHeader("username", username);
        httpGet.addHeader("password", password);
        CloseableHttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpGet);
        System.out.println(httpResponse);
    }

    public void RedeemGiftCard(){

    }

    public void ReloadGiftCard(){

    }

    public void CancelLastOperation(){

    }
}
