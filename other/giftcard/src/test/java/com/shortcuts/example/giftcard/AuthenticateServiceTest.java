package com.shortcuts.example.giftcard;

import com.shortcuts.example.giftcard.Models.AuthenticateRequest;
import com.shortcuts.example.giftcard.Models.AuthenticateResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class AuthenticateServiceTest {

    String testJwtToken;

    @Before
    public void setupTest(){
        AuthenticateRequest authenticateRequest =
                new AuthenticateRequest(
                        "serial",
                        "BWQK6ARB77JJ",
                        "5C3EEC2C-4820-45DD-92A5-505DBF60D7CE"
                );

        AuthenticateResponse authenticateResponse = new AuthenticateService().authenticate(authenticateRequest);

        if(authenticateResponse.getError_type_code() == null){
            testJwtToken = authenticateResponse.getAccessToken();
        }
    }

    @Test
    public void testAuthenticate(){
        AuthenticateRequest authenticateRequest =
                new AuthenticateRequest(
                        "serial",
                        "BWQK6ARB77JJ",
                        "5C3EEC2C-4820-45DD-92A5-505DBF60D7CE"
                );

        AuthenticateResponse authenticateResponse = new AuthenticateService().authenticate(authenticateRequest);
        System.out.println(authenticateResponse);
        assertTrue(authenticateResponse.getError_type_code() == null);
    }

    @Test
    public void testBalanceInquire(){
        GiftCardService service = new GiftCardService();
        service.balanceInquire("62997400000000534581", testJwtToken, "BWQK6ARB77JJ", "password");
    }
}