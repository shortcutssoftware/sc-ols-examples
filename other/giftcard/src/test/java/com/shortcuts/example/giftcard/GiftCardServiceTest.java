package com.shortcuts.example.giftcard;

import com.shortcuts.example.giftcard.Models.RequestBody.*;
import com.shortcuts.example.giftcard.Models.RequestHeader;
import com.shortcuts.example.giftcard.Models.Response.AuthenticateResponse;
import com.shortcuts.example.giftcard.Models.Response.BaseResponse;
import com.shortcuts.example.giftcard.Models.Response.CardServiceResponse;
import com.shortcuts.example.giftcard.Models.Response.TransactionResponse;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class GiftCardServiceTest {

    private static final String TEST_INSTALLATION_ID = "5C3EEC2C-4820-45DD-92A5-505DBF60D7CE";

    private static final String TEST_SERIAL_NUMBER = "BWQK6ARB77JJ";

    //TODO: delete once prod giftcard deployed
    private static final String TEST_PASSWORD = "password";

    private String testJwtToken;

    private RequestHeader requestHeader;

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
            requestHeader = new RequestHeader(testJwtToken, TEST_SERIAL_NUMBER, TEST_PASSWORD);
        }
    }

    @Test
    public void testBalanceInquireError(){
        CardServiceResponse response = giftCardService.balanceInquire("62997400000000534581", requestHeader);
        assertTrue(response.getError_type_code().equals(BaseResponse.ERROR_TYPE_CODE.system));
        //assertEquals(response.getMessage(), "Card 62997400000000534581 not registered");
    }

    @Test
    public void testBalanceInquireSuccessful(){
        CardServiceResponse response = giftCardService.balanceInquire("62997400000001248644", requestHeader);
        System.out.println(response.getMemberBalanceResponse().getBalanceExTaxAmount());
    }

    @Test
    public void ActivateCardSuccessful(){
        ActivateCardRequestBody request = new ActivateCardRequestBody();
        request.setSiteTransactionId("1");
        request.setSiteTransactionDateTime("2018-06-01T23:35:35.05");
        request.setActivationIncTaxAmount(new BigDecimal(500.00));
        TransactionResponse response = giftCardService.activateGiftCard("62997400000001248644", requestHeader, request);
        System.out.println(response);
    }

    @Test
    public void ActivateCardFail(){
        ActivateCardRequestBody request = new ActivateCardRequestBody();
        request.setSiteTransactionId("#");
        request.setSiteTransactionDateTime("");
        request.setActivationIncTaxAmount(new BigDecimal(00.00));
        TransactionResponse response = giftCardService.activateGiftCard("62997400000001248644", requestHeader, request);
        assertTrue(response.getError_type_code().equals(BaseResponse.ERROR_TYPE_CODE.system));
    }

    @Test
    public void RedeemCardSuccessful(){
        RedeemCardRequestBody request = new RedeemCardRequestBody();
        request.setSiteTransactionId("2");
        request.setSiteTransactionDateTime("2018-06-01T23:36:42.04");
        request.setRedemptionAmount(new BigDecimal(50.00));
        CardServiceResponse response = giftCardService.redeemGiftCard("62997400000001248644", requestHeader, request);
        System.out.println(response);
    }

    @Test
    public void ReloadCardSuccessful(){
        ReloadCardRequestBody request = new ReloadCardRequestBody();
        request.setSiteTransactionId("3");
        request.setSiteTransactionDateTime("2018-06-01T23:37:42.04");
        request.setReloadAmount(new BigDecimal(250.00));
        TransactionResponse response = giftCardService.reloadGiftCard("62997400000001248644", requestHeader, request);
        System.out.println(response);
    }

    @Test
    public void CancelLastOperationSuccessful(){
        CancelLastRequestBody request = new CancelLastRequestBody();
        request.setSiteTransactionId("4");
        request.setSiteTransactionDateTime("2018-06-01T23:40:42.04");
        request.setOriginalSiteTransactionId("3");
        request.setOriginalTransactionAmount(new BigDecimal(250.00));
        CardServiceResponse response = giftCardService.cancelLastOperation("62997400000001248644", requestHeader, request);
        System.out.println(response);
    }
}
