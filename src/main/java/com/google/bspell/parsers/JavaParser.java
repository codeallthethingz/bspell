package com.google.bspell.parsers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JavaParser extends AbstractParser {
    
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
         "get", "set", "io", "out", "src", "jaxb", "jaxws", "wsdl4j", "xmlns", "pom"
     ));
    
    public boolean isJavaKeyword(final String keyword) {
        return KEYWORDS.contains(keyword); 
    }

    public boolean isExcluded(final String keyword) {
        return isJavaKeyword(keyword) || EXCLUDES.contains(keyword);
    }

}