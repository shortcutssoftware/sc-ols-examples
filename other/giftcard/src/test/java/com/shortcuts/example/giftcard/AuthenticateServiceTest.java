package com.shortcuts.example.giftcard;

import org.junit.Test;

public class AuthenticateServiceTest {

    @Test
    public void testAuthenticate(){
        AuthenticateRequest authenticateRequest =
                new AuthenticateRequest(
                        "serial",
                        "BWQK6ARB77JJ",
                        "5C3EEC2C-4820-45DD-92A5-505DBF60D7CE"
                );

        AuthenticateResponse authenticateResponse = new AuthenticateService().authenticate(authenticateRequest);
    }
}