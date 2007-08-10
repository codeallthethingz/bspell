package com.google.bspell.parsers;

import java.io.File;
import java.util.List;
import java.util.Set;

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
        File file = new File(getClass().getResource("resources/test.registry").toURI());
        try {
            ParserFactory.getInstance().getParser(file);
            fail("registry is not supported");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().indexOf("[registry]") != -1);
        }
    }
    
    @Test
    public  void testInitRegistry() throws Exception {
        File file = new File(getClass().getResource("resources/test.registry").toURI());
        assertFalse(ParserFactory.getInstance().getRegistry().containsKey("txt"));
        ParserFactory.getInstance().initRegistry(file.toString());
        assertTrue(ParserFactory.getInstance().getRegistry().containsKey("txt"));
    }

    @Test
    public  void testInitReserved() throws Exception {
        File file = new File(getClass().getResource("resources/test.reserved").toURI());
        ParserFactory.getInstance().initExcludes(file.toString());
        Set<String> excludes = ParserFactory.getInstance().getParser("java").getUserExcludes();
        assertTrue(excludes.contains("wiki"));
        assertTrue(excludes.contains("jaxws"));
    }
 }