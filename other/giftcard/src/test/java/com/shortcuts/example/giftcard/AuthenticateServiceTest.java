package com.shortcuts.example.giftcard;

import com.shortcuts.example.giftcard.Models.RequestBody.AuthenticateRequestBody;
import com.shortcuts.example.giftcard.Models.Response.AuthenticateResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class AuthenticateServiceTest {

    String testJwtToken;

    @Before
    public void setupTest(){
        AuthenticateRequestBody authenticateRequestBody =
                new AuthenticateRequestBody(
                        "serial",
                        "BWQK6ARB77JJ",
                        "5C3EEC2C-4820-45DD-92A5-505DBF60D7CE"
                );

        AuthenticateResponse authenticateResponse = new AuthenticateService().authenticate(authenticateRequestBody);

        if(authenticateResponse.getError_type_code() == null){
            testJwtToken = authenticateResponse.getAccessToken();
        }
    }

    @Test
    public void testAuthenticate(){
        AuthenticateRequestBody authenticateRequestBody =
                new AuthenticateRequestBody(
                        "serial",
                        "BWQK6ARB77JJ",
                        "5C3EEC2C-4820-45DD-92A5-505DBF60D7CE"
                );

        AuthenticateResponse authenticateResponse = new AuthenticateService().authenticate(authenticateRequestBody);
        assertTrue(authenticateResponse.getError_type_code() == null);
    }
}