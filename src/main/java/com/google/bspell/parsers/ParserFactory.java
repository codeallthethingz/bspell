package com.google.bspell.parsers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.bspell.utils.StringUtils;

public class ParserFactory {
    private static final ParserFactory INSTANCE = new ParserFactory();

    Map<String, Parser> registry = new HashMap<String, Parser>();

    public static ParserFactory getInstance() {
        return INSTANCE;
    }

    public Map<String, Parser> getRegistry() {
        return registry;
    }

    public Parser getParser(final File file) {
        String name = file.getName();
        return getParser(name.substring(name.lastIndexOf(".") + 1));
    }

    public Parser getParser(final String extension) {
        try {
            Parser parser = registry.get(extension);
            if (parser == null) {
                parser = (Parser) Class.forName("com.google.bspell.parsers." + StringUtils.capitalize(extension) + "Parser").newInstance();
                registry.put(extension, parser);
            }
            return parser;
        } catch (Exception e) {
            throw new RuntimeException("We are not support file extension [" + extension 
                                       + "] yet, or it's not in the registry!");
        }
    }
}