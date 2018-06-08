package com.shortcuts.example.giftcard;

import com.shortcuts.example.giftcard.Models.RequestBody.AuthenticateRequestHeaders;
import com.shortcuts.example.giftcard.Models.Response.AuthenticateResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertTrue;


public class AuthenticateServiceTest {

    Properties properties;

    @Before
    public void setupTest(){
        try{
            Properties properties = new Properties();
            File file = new File(new File("."), "test.properties").getAbsoluteFile();
            if (file.exists()) {
                properties.load(new FileInputStream(file));
            }
            this.properties = properties;
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testAuthenticate(){
        AuthenticateRequestHeaders authenticateRequestHeaders = new AuthenticateRequestHeaders(
                properties.getProperty("oauth.consumer_key"),
                properties.getProperty("oauth.consumer_secret"),
                properties.getProperty("oauth.access_token_key"),
                properties.getProperty("oauth.access_token_secret")
        );
        AuthenticateResponse authenticateResponse = new AuthenticateService(authenticateRequestHeaders).authenticate();
        assertTrue(authenticateResponse.getError_type_code() == null);
    }
}