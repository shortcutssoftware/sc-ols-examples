package com.shortcuts.example.giftcard;

import com.shortcuts.example.giftcard.Models.RequestBody.AuthenticateRequestBody;
import com.shortcuts.example.giftcard.Models.Response.AuthenticateResponse;
import com.shortcuts.example.giftcard.Models.Response.BaseResponse;
import com.shortcuts.example.giftcard.Models.Response.CardServiceResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GiftCardServiceTest {

    String testJwtToken;

    @Before
    public void setupAuthentication(){
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
    public void testBalanceInquireError(){
        GiftCardService service = new GiftCardService();
        CardServiceResponse response = service.balanceInquire("62997400000000534581", testJwtToken, "BWQK6ARB77JJ", "password");
        assertTrue(response.getError_type_code().equals(BaseResponse.ERROR_TYPE_CODE.system));
        //assertEquals(response.getMessage(), "Card 62997400000000534581 not registered");
    }

    @Test
    public void testBalanceInquireSuccessful(){
        GiftCardService service = new GiftCardService();
        CardServiceResponse response = service.balanceInquire("62997400000001248546", testJwtToken, "BWQK6ARB77JJ", "password");
        System.out.println(response.getMemberBalanceResponse().getBalanceExTaxAmount());
    }
}
