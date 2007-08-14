package com.google.bspell.parsers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TxtParser extends AbstractParser {

    public static final Set<String> EXCLUDES = new HashSet<String>(Arrays.asList(
         "println", "util", "args", "argv", "main", "millis", "txt"
                                                                                 ));
    
    public boolean isExcluded(final String keyword) {
        return EXCLUDES.contains(keyword);
    }

}