package com.google.bspell.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.bspell.model.Location;
import com.google.bspell.model.Word;

public abstract class AbstractParser implements Parser {
    public static final String PUNCTUATION = new String(" ._,()[]{}-+;<>*&^%$#@!~`|\\/:\"?\n\t\r=");

    public static final char[] LOWER_CASE = new char[] {'a', 'z'};
    public static final char[] UPPER_CASE = new char[] {'A', 'Z'};
    
    public Set<String> userExcludes = new HashSet<String>();

    public Set<String> getUserExcludes() {
        return userExcludes;
    }

    public boolean isUserExcluded(final String keyword) {
        return userExcludes.contains(keyword);
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

    public abstract boolean isExcluded(final String word);

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
                    String v = word.getValue().toString().toLowerCase();
                    if (!isExcluded(v) && !isUserExcluded(v)) {
                        words.add(word);                            
                    }
                }
                word = newWord(lineNum, i + 1);
            } else if (!isPunctuation(previousChar)) {
                word.getValue().append(previousChar);                        
            }
        }
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