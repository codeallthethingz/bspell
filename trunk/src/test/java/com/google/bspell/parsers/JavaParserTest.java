package com.google.bspell.parsers;

import java.io.File;
import java.util.List;

import com.google.bspell.model.Word;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;

public class JavaParserTest extends Assert {
    JavaParser parser = new JavaParser();

    @Test
    public void testIsPunctuation() {
        char[] chars = JavaParser.PUNCTUATION.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            assertTrue(parser.isPunctuation(chars[i]));
        }
        assertFalse(parser.isPunctuation('a'));
    }

    @Test
    public void testIsLowerCase() {
        assertTrue(parser.isLowerCase('a'));
        assertTrue(parser.isLowerCase('m'));
        assertTrue(parser.isLowerCase('z'));
        
        assertFalse(parser.isLowerCase('A'));
    }

    @Test
    public void testIsUpperCase() {
        assertTrue(parser.isUpperCase('A'));
        assertTrue(parser.isUpperCase('M'));
        assertTrue(parser.isUpperCase('Z'));
        
        assertFalse(parser.isUpperCase('a'));
    }

    @Test
    public void testParseFile() throws Exception {
        File excludes = new File(getClass().getResource("resources/test.reserved").toURI());
        ParserFactory.getInstance().initExcludes(excludes.toString());
        Parser p = ParserFactory.getInstance().getParser("java");

        File file = new File(getClass().getResource("resources/test.java.txt").toURI());
        List<Word> words = p.parse(file);
        assertTrue(words.get(0).equals("com"));
        assertEquals(51, words.size());
    }

    @Test
    public void testParseLine() throws Exception {
        List<Word> words = new ArrayList<Word>();
        parser.parse(1, "int i = 0; static final float MY_FIRST_STATIC_FLOAT = 1F; class HelloWorld", words);

        assertEquals(3, words.size());
        assertTrue(words.get(0).equals("FIRST"));
    }
}