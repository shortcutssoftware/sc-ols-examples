package com.shortcuts.example.java;

import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class BaseUrlAwareTest {

    @Test
    public void testGetEndpoint() {
        BaseUrlAware baseUrlAware = new BaseUrlAware();
        baseUrlAware.setBaseUrl("http://example.com");
        assertEquals("http://example.com/", baseUrlAware.getEndpointURI(null).toString());
        assertEquals("http://example.com/", baseUrlAware.getEndpointURI("").toString());
        assertEquals("http://example.com/a/b/c", baseUrlAware.getEndpointURI("a/b/c").toString());
        assertEquals("http://example.com/a/1/2", baseUrlAware.getEndpointURI("a/{b}/{c}", "1", "2").toString());
        assertEquals("http://example.com/a/b/c", baseUrlAware.getEndpointURI("a/b/c", Optional.empty()).toString());
        LinkedMultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<>();
        assertEquals("http://example.com/a/b/c", baseUrlAware.getEndpointURI("a/b/c", Optional.of(queryParameters)).toString());
        queryParameters.add("x", "y");
        queryParameters.put("z", Arrays.asList("1", "2", "3"));
        assertEquals("http://example.com/a/b/c?x=y&z=1&z=2&z=3", baseUrlAware.getEndpointURI("a/b/c", Optional.of(queryParameters)).toString());
        assertEquals("http://example.com/a/1/2?x=y&z=1&z=2&z=3", baseUrlAware.getEndpointURI("a/{b}/{c}", Optional.of(queryParameters), 1, 2).toString());
    }

}