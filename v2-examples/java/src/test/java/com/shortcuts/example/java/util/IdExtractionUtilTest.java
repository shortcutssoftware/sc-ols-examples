package com.shortcuts.example.java.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IdExtractionUtilTest {

    @Test
    public void testIdExtraction() {
        IdExtractionUtil idExtractionUtil = new IdExtractionUtil();
        assertNull(idExtractionUtil.extractId("", ""));
        assertNull(idExtractionUtil.extractId("site", ""));
        assertEquals("35648", idExtractionUtil.extractId("site", "https://api.shortcutssoftware.io/site/35648"));
        assertEquals("35648", idExtractionUtil.extractId("site", "https://api.shortcutssoftware.io/site/35648/another/site/12345678"));
    }

}