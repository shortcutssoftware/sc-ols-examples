package com.shortcuts.example.java.services.site;

import com.shortcuts.example.java.authentication.JWTSerialNumberAuthenticationService;
import com.shortcuts.example.java.util.IdExtractionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetSitesServiceTest {

    @Autowired
    JWTSerialNumberAuthenticationService jwtSerialNumberAuthenticationService;

    @Autowired
    GetSitesService getSitesService;

    @Autowired
    GetSiteService getSiteService;

    @Test
    public void testSitesGet() {

        String jwtToken = jwtSerialNumberAuthenticationService.authenticate();
        GetSitesResponse getSitesResponse = getSitesService.call(
                jwtToken,
                Optional.empty(),
                Optional.empty());

        assertNotNull(getSitesResponse);
        assertFalse(getSitesResponse.getSites().isEmpty());

        String href = getSitesResponse.getSites().get(0).getHref();
        String siteId = new IdExtractionUtil().extractId("site", href);

        Site site = getSiteService.call(
                jwtToken,
                Optional.empty(),
                Optional.empty(),
                siteId);

        assertTrue(site.getIs_active());
        assertFalse(site.getLinks().isEmpty());

    }

}