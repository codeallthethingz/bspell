package com.google.bspell.parsers;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class ParserFactoryTest extends Assert {
    @Test
    public void testGetParser() throws Exception {
        Parser parser = ParserFactory.getInstance().getParser("java");
        assertTrue(parser instanceof JavaParser);
    }

    @Test
    public void testNotSupported() throws Exception {
        File file = new File(getClass().getResource("resources/test.java.txt").toURI());
        try {
            ParserFactory.getInstance().getParser(file);
            fail("TXT is not supported");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().indexOf("[txt]") != -1);
        }
    }
}