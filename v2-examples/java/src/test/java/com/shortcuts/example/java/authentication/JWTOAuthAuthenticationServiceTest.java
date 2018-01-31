package com.shortcuts.example.java.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class JWTOAuthAuthenticationServiceTest {

    @Autowired
    JWTOAuthAuthenticationService jwtoAuthAuthenticationService;

    @Test
    public void testConfigurationIsAvailable() {
        assertTrue(jwtoAuthAuthenticationService.getBaseUrl().startsWith("https://"));
        assertNotNull(jwtoAuthAuthenticationService.getRestTemplateCallingUtil());
        assertNotNull(jwtoAuthAuthenticationService.getConsumerKey());
        assertNotNull(jwtoAuthAuthenticationService.getConsumerSecret());
        assertNotNull(jwtoAuthAuthenticationService.getAccessKey());
        assertNotNull(jwtoAuthAuthenticationService.getAccessSecret());
    }

    @Test
    public void testAuthenticate() {
        String jwtToken = jwtoAuthAuthenticationService.authenticate();
        assertNotNull(jwtToken);
    }

}