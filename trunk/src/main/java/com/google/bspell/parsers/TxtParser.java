package com.google.bspell.parsers;

public class TxtParser extends AbstractParser {
    
    public boolean isExcluded(final String keyword) {
        //return EXCLUDES.contains(keyword);
        return false;
    }

}