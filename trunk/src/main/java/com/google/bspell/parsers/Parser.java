package com.google.bspell.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.google.bspell.model.Word;

public interface Parser {
    List<Word> parse(final File file) throws FileNotFoundException, IOException;
    public Set<String> getUserExcludes();
}