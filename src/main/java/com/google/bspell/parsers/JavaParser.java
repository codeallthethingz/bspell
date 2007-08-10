package com.google.bspell.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.bspell.model.Location;
import com.google.bspell.model.Word;

public class JavaParser implements Parser {
    public static final String PUNCTUATION = new String(" ._,()[]{}-+;<>*&^%$#@!~`|\\/:\"?\n\t\r=");
    
    public static final Set<String> KEYWORDS = new HashSet<String>(Arrays.asList(
        "abstract", "assert", "boolean", "break", "byte", "case", "catch",
        "char", "class", "const", "continue", "default", "do", "double",
        "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto",
        "if", "implements", "import", "instanceof", "int", "interface", "long",
        "native", "new", "null", "package", "private", "protected", "public",
        "return", "short", "static", "strictfp", "super", "switch",
        "synchronized", "this", "throw", "throws", "transient", "true", "try",
        "void", "volatile", "while"
    ));

    public static final Set<String> EXCLUDES = new HashSet<String>(Arrays.asList(
         "println", "util", "args", "argv", "main", "Millis",
         "get", "set", "io", "out", "src"
     ));
    
    public static final char[] LOWER_CASE = new char[] {'a', 'z'};
    public static final char[] UPPER_CASE = new char[] {'A', 'Z'};
    
    public static boolean isJavaKeyword(String keyword) {
        return KEYWORDS.contains(keyword); 
    }

    public static boolean isExcluded(String keyword) {
        return EXCLUDES.contains(keyword);
    }

    protected boolean isPunctuation(char c) {
        return PUNCTUATION.indexOf(c) != -1;
    }

    protected boolean isLowerCase(char c) {
        return c >= LOWER_CASE[0] && c <= LOWER_CASE[1];
    }

    protected boolean isUpperCase(char c) {
        return c >= UPPER_CASE[0] && c <= UPPER_CASE[1];
    }

    public List<Word> parse(final File javaFile) throws FileNotFoundException, IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader(javaFile));
        final List<Word> words = new ArrayList<Word>();
        String line = reader.readLine();
        
        while (line != null) {
            parse(reader.getLineNumber(), line, words);
            line = reader.readLine();
        }
        reader.close();
        return words;
    }

    protected void parse(int lineNum, final String line, final List<Word> words) {
        char[] chars = line.toCharArray();

        boolean start = true;
            
        Word word = newWord(1, 1);

        for (int i = 1; i <= chars.length; i++) {
            char previousChar = chars[i - 1];
            char currentChar = ' ';

            if (i != chars.length) {
                currentChar = chars[i];
            }
            
            if (isPunctuation(currentChar) || isLowerCase(previousChar) && isUpperCase(currentChar)) {
                if (!isPunctuation(previousChar)) {
                    word.getValue().append(previousChar);
                }
                if (words.contains(word)) {
                    addLocation(words, word);
                } else if (word.getValue().length() > 2) {
                    String v = word.getValue().toString();
                    if (!isJavaKeyword(v) && !isExcluded(v)) {
                        words.add(word);                            
                    }
                }
                word = newWord(lineNum, i + 1);
            } else if (!isPunctuation(previousChar)) {
                word.getValue().append(previousChar);                        
            }
        }
    }

    private void addLocation(final List<Word> words, final Word word) {
        words.get(words.indexOf(word)).getLocations().addAll(word.getLocations());
    }

    private Word newWord(int line, int column) {
        Word word = new Word();
        Location location = new Location();
        location.setLine(line);
        location.setColumn(column);
        word.getLocations().add(location);
        return word;
    }
}