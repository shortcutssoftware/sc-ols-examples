package com.shortcuts.example.java.services.site;

import com.shortcuts.example.java.authentication.JWTSerialNumberAuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetSitesServiceTest {

    @Autowired
    JWTSerialNumberAuthenticationService jwtSerialNumberAuthenticationService;

    @Autowired
    GetSitesService getSitesService;

    @Test
    public void testSitesGet() {

        String jwtToken = jwtSerialNumberAuthenticationService.authenticate();
        GetSitesResponse getSitesResponse = getSitesService.call(
                jwtToken,
                new HttpHeaders(),
                new GetSitesRequest());

        assertNotNull(getSitesResponse);
        assertTrue(!getSitesResponse.getSites().isEmpty());
    }

}