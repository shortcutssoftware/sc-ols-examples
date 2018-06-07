package com.shortcuts.example.giftcard;

import com.shortcuts.example.giftcard.Models.RequestBody.*;
import com.shortcuts.example.giftcard.Models.RequestHeaders;
import com.shortcuts.example.giftcard.Models.Response.AuthenticateResponse;
import com.shortcuts.example.giftcard.Models.Response.CardServiceResponse;
import com.shortcuts.example.giftcard.Models.Response.TransactionResponse;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GiftCardServiceTest {

    private static final String TEST_INSTALLATION_ID = "5C3EEC2C-4820-45DD-92A5-505DBF60D7CE";

    private static final String TEST_SERIAL_NUMBER = "BWQK6ARB77JJ";

    private String testJwtToken;

    private RequestHeaders requestHeaders;

    private GiftCardService giftCardService = new GiftCardService();

    @Before
    public void setupAuthentication(){
        AuthenticateRequestBody authenticateRequestBody =
                new AuthenticateRequestBody(
                        "serial",
                        TEST_SERIAL_NUMBER,
                        TEST_INSTALLATION_ID
                );

        AuthenticateResponse authenticateResponse = new AuthenticateService().authenticate(authenticateRequestBody);

        if(authenticateResponse.getError_type_code() == null){
            testJwtToken = authenticateResponse.getAccessToken();
            requestHeaders = new RequestHeaders(testJwtToken, TEST_SERIAL_NUMBER);
        }
    }

    @Test
    public void testBalanceInquireError(){
        try{
            giftCardService.balanceInquire("62997400000000534581", requestHeaders);
        } catch (RuntimeException actual){
            assertTrue(actual.getMessage().contains("Error Type: system, Card '62997400000000534581' cannot be used at this site."));
        }
    }

    @Test
    public void testBalanceInquireSuccessful(){
        CardServiceResponse response = giftCardService.balanceInquire("62997400000001248644", requestHeaders);
        assertEquals("0", response.getMemberBalanceResponse().getBalanceExTaxAmount().toString());
    }

    @Test
    public void ActivateCardSuccessful(){
        BigDecimal activationAmount = new BigDecimal(500.00);

        ActivateCardRequestBody request = new ActivateCardRequestBody();
        request.setSiteTransactionId("-1");
        request.setSiteTransactionDateTime("2018-06-01T23:35:35.05");
        request.setActivationIncTaxAmount(activationAmount);
        TransactionResponse response = giftCardService.activateGiftCard("62997400000001248645", requestHeaders, request);
        assertEquals("500", response.getTransactionExTaxAmount().toString());
    }

    @Test
    public void ActivateCardFail(){
        ActivateCardRequestBody request = new ActivateCardRequestBody();
        request.setSiteTransactionId("");
        request.setSiteTransactionDateTime("2018-06-01T23:35:35.05");
        request.setActivationIncTaxAmount(new BigDecimal(00.00));

        try{
            giftCardService.activateGiftCard("62997400000001248644", requestHeaders, request);
        } catch (RuntimeException actual){
            assertTrue(actual.getMessage().contains("Error Type: system, There was an unspecified error on the server.  Please try the transaction again or contact Shortcuts support."));
        }
    }

    @Test
    public void RedeemCardSuccessful(){
        /**
         * new balance = Activation amount - redemption amount
         */
        BigDecimal redemptionAmount = new BigDecimal(50.00);
        BigDecimal balanceAmount = new BigDecimal(0.00);

        RedeemCardRequestBody request = new RedeemCardRequestBody();
        request.setSiteTransactionId("-1");
        request.setSiteTransactionDateTime("2018-06-01T23:36:42.04");
        request.setRedemptionAmount(redemptionAmount);
        CardServiceResponse response = giftCardService.redeemGiftCard("62997400000001248645", requestHeaders, request);
        assertEquals("-50" ,response.getTransactionExTaxAmount().toString());
        assertEquals("450.0000", response.getMemberBalanceResponse().getBalanceExTaxAmount().toString());
    }

    @Test
    public void ReloadCardSuccessful(){
        BigDecimal reloadAmount = new BigDecimal(20.00);

        ReloadCardRequestBody request = new ReloadCardRequestBody();
        request.setSiteTransactionId("-1");
        request.setSiteTransactionDateTime("2018-06-01T23:37:42.04");
        request.setReloadAmount(reloadAmount);
        TransactionResponse response = giftCardService.reloadGiftCard("62997400000001248645", requestHeaders, request);
        assertEquals("20", response.getTransactionExTaxAmount().toString());
    }

    @Test
    public void CancelLastOperationSuccessful(){
        BigDecimal cancelLastAmount = new BigDecimal(20.00);

        CancelLastRequestBody request = new CancelLastRequestBody();
        request.setSiteTransactionId("-1");
        request.setSiteTransactionDateTime("2018-06-01T23:40:42.04");
        request.setOriginalSiteTransactionId("-1");
        request.setOriginalTransactionAmount(cancelLastAmount);
        CardServiceResponse response = giftCardService.cancelLastOperation("62997400000001248645", requestHeaders, request);
        assertEquals("450.0000", response.getMemberBalanceResponse().getBalanceExTaxAmount().toString());
    }
}
