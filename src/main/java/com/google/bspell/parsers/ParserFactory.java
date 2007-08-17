package com.google.bspell.parsers;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.bspell.model.SpellConfiguration;
import com.google.bspell.utils.PropertiesUtils;
import com.google.bspell.utils.StringUtils;

public final class ParserFactory {
    private static ParserFactory instance;
    private final SpellConfiguration config;

    private Map<String, Parser> registry = new HashMap<String, Parser>();
    private Map<String, List<String>> excludesMap = new HashMap<String, List<String>>();

    private ParserFactory(SpellConfiguration c) {
        this.config = c;
        initRegistry();
    }

    private void initRegistry() {
        if (config == null) {
            return;
        }

        try {
            initExcludes(config.getReservedDict());
            initRegistry(config.getRegistry());
        } catch (Exception e) {
            System.err.println("Warning: Initialization failed.");
        }
    }

    protected void initExcludes(final String reservedDict) throws Exception {
        Properties properties = PropertiesUtils.loadProperties(reservedDict);

        Enumeration<?> extensions = properties.propertyNames();
        while (extensions.hasMoreElements()) {
            String extension  = (String) extensions.nextElement();
            excludesMap.put(extension, StringUtils.tokenize(properties.getProperty(extension)));
        }
    }

    protected void initRegistry(final String r) throws Exception {
        Properties properties = PropertiesUtils.loadProperties(r);
        Enumeration<?> extensions = properties.propertyNames();
        while (extensions.hasMoreElements()) {
            String extension  = (String) extensions.nextElement();
            newParser(extension, properties.getProperty(extension));
        }
    }

    public static ParserFactory getInstance() {
        return getInstance(null);
    }

    public static ParserFactory getInstance(SpellConfiguration c) {
        if (instance == null) {
            instance = new ParserFactory(c);
        }
        return instance;
    }

    public Map<String, Parser> getRegistry() {
        return registry;
    }

    public Parser getParser(final File file) {
        String name = file.getName();
        return getParser(name.substring(name.lastIndexOf(".") + 1));
    }

    public Parser getParser(final String extension) {
        Parser parser = registry.get(extension);
        if (parser == null) {
            return newParser(extension, "com.google.bspell.parsers." + StringUtils.capitalize(extension) + "Parser");
        }
        return parser;
    }

    public Parser newParser(final String extension, final String fullClassName) {
        try {        
            Parser parser = (Parser) Class.forName(fullClassName).newInstance();
            if (excludesMap.get(extension) != null) {
                parser.getUserExcludes().addAll(excludesMap.get(extension));
            }
            parser.getUserExcludes().addAll(excludesMap.get("general"));
            registry.put(extension, parser);
            return parser;
        } catch (Exception e) {
            throw new RuntimeException("We are not support file extension [" + extension 
                                       + "] yet, or it's not in the registry!");
        }
    }
}