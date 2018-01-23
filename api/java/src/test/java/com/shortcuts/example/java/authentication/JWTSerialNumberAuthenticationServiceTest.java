package com.shortcuts.example.java.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class JWTSerialNumberAuthenticationServiceTest {

    @Autowired
    JWTSerialNumberAuthenticationService jwtSerialNumberAuthenticationService;

    @Test
    public void testConfigurationIsAvailable() {
        assertTrue(jwtSerialNumberAuthenticationService.getBaseUrl().startsWith("https://"));
        assertNotNull(jwtSerialNumberAuthenticationService.getRestTemplate());
        assertNotNull(jwtSerialNumberAuthenticationService.getSerialNumber());
        assertNotNull(jwtSerialNumberAuthenticationService.getSiteInstallationId());
    }

    @Test
    public void testAuthenticate() {
        String jwtToken = jwtSerialNumberAuthenticationService.authenticate();
        assertNotNull(jwtToken);
    }

}