package com.shortcuts.example.java;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseUrlAwareTest {

    @Test
    public void testGetEndpoint() {
        BaseUrlAware baseUrlAware = new BaseUrlAware();
        baseUrlAware.setBaseUrl("http://example.com");
        assertEquals("http://example.com/", baseUrlAware.getEndpoint(null));
        assertEquals("http://example.com/", baseUrlAware.getEndpoint(""));
        assertEquals("http://example.com/a/b/c", baseUrlAware.getEndpoint("a", "b", "c"));
    }

}