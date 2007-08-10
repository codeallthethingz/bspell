package com.google.bspell.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest extends Assert {
    @Test
    public void testCapitalize() {
        assertEquals("Mao", StringUtils.capitalize("mao"));
        assertEquals("Mao", StringUtils.capitalize("Mao"));
    }
}