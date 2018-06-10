package com.shortcuts.example.giftcard;

import com.shortcuts.example.giftcard.Models.RequestBody.*;
import com.shortcuts.example.giftcard.Models.RequestHeaders;
import com.shortcuts.example.giftcard.Models.Response.AuthenticateResponse;
import com.shortcuts.example.giftcard.Models.Response.CardServiceResponse;
import com.shortcuts.example.giftcard.Models.Response.TransactionResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class GiftCardServiceTest {

    private String siteSerialNumber;

    private String testJwtToken;

    private RequestHeaders requestHeaders;

    private GiftCardService giftCardService = new GiftCardService();

    private Properties properties;

    private String giftCardNumber;

    @Before
    public void setupAuthentication(){
        try{
            properties = getTestProperties();
            siteSerialNumber = properties.getProperty("site_serial_number");
            giftCardNumber = properties.getProperty("giftcard.registered.ready");
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }

        AuthenticateRequestHeaders authenticateRequestHeaders = new AuthenticateRequestHeaders(
                properties.getProperty("oauth.consumer_key"),
                properties.getProperty("oauth.consumer_secret"),
                properties.getProperty("oauth.access_token_key"),
                properties.getProperty("oauth.access_token_secret")
        );

        AuthenticateResponse authenticateResponse = new AuthenticateService(authenticateRequestHeaders).authenticate();

        if(authenticateResponse.getError_type_code() == null){
            testJwtToken = authenticateResponse.getAccessToken();
            requestHeaders = new RequestHeaders(testJwtToken, siteSerialNumber);
        }

    }

    @Test
    public void giftCardOperationTests(){
        activateGiftCard();

        balanceInquire();

        redeemGiftCard();

        reloadGiftCard();

        cancelLastOperation();
    }

    public void activateGiftCard(){
        BigDecimal activationAmount = new BigDecimal(200.00);

        ActivateCardRequestBody request = new ActivateCardRequestBody();
        request.setSiteTransactionId("0");
        request.setSiteTransactionDateTime(LocalDateTime.now());
        request.setActivationIncTaxAmount(activationAmount);
        TransactionResponse response = giftCardService.activateGiftCard(giftCardNumber, requestHeaders, request);
        assertEquals("200", response.getTransactionExTaxAmount().toString());
        System.out.println(String.format("Activation amount: %s", activationAmount));
    }


    public void balanceInquire(){
        BigDecimal balance = new BigDecimal(200.00);
        CardServiceResponse response = giftCardService.balanceInquire(giftCardNumber, requestHeaders);
        assertEquals("200.0000", response.getMemberBalanceResponse().getBalanceExTaxAmount().toString());
        System.out.println(String.format("Giftcard balance: %s", balance));
    }

    public void redeemGiftCard(){
        /**
         * new balance = Activation amount - redemption amount
         */
        BigDecimal redemptionAmount = new BigDecimal(50.00);
        BigDecimal balance = new BigDecimal(150.00);
        RedeemCardRequestBody request = new RedeemCardRequestBody();
        request.setSiteTransactionId("0");
        request.setSiteTransactionDateTime(LocalDateTime.now());
        request.setRedemptionAmount(redemptionAmount);
        CardServiceResponse response = giftCardService.redeemGiftCard(giftCardNumber, requestHeaders, request);
        assertEquals("-50" ,response.getTransactionExTaxAmount().toString());
        assertEquals("150.0000", response.getMemberBalanceResponse().getBalanceExTaxAmount().toString());
        System.out.println(String.format("Redeem amount: %s, Balance: %s", redemptionAmount, new BigDecimal(150.00)));
    }

    public void reloadGiftCard(){
        BigDecimal reloadAmount = new BigDecimal(20.00);
        BigDecimal balance = new BigDecimal(170.00);
        ReloadCardRequestBody request = new ReloadCardRequestBody();
        request.setSiteTransactionId("0");
        request.setSiteTransactionDateTime(LocalDateTime.now());
        request.setReloadAmount(reloadAmount);
        TransactionResponse response = giftCardService.reloadGiftCard(giftCardNumber, requestHeaders, request);
        assertEquals("20", response.getTransactionExTaxAmount().toString());
        System.out.println(String.format("Reload amount: %s, Balance: %s", reloadAmount, balance));
    }

    public void cancelLastOperation(){
        BigDecimal cancelLastAmount = new BigDecimal(20.00);
        BigDecimal balance = new BigDecimal(150.00);
        CancelLastRequestBody request = new CancelLastRequestBody();
        request.setSiteTransactionId("0");
        request.setSiteTransactionDateTime(LocalDateTime.now());
        request.setOriginalSiteTransactionId("0");
        request.setOriginalTransactionAmount(cancelLastAmount);
        CardServiceResponse response = giftCardService.cancelLastOperation(giftCardNumber, requestHeaders, request);
        assertEquals("150.0000", response.getMemberBalanceResponse().getBalanceExTaxAmount().toString());
        System.out.println(String.format("Cancel last transaction: %s, balance: %s", cancelLastAmount, balance));
    }

    private Properties getTestProperties() throws IOException {
        Properties properties = new Properties();
        File file = new File(new File("."), "test.properties").getAbsoluteFile();
        if (file.exists()) {
            properties.load(new FileInputStream(file));
        }
        return properties;
    }
}
