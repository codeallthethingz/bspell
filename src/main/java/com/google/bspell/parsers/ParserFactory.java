package com.google.bspell.parsers;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.bspell.model.Configuration;
import com.google.bspell.utils.PropertiesUtils;
import com.google.bspell.utils.StringUtils;

public class ParserFactory {
    private static ParserFactory INSTANCE;
    private final Configuration config;

    Map<String, Parser> registry = new HashMap<String, Parser>();

    private ParserFactory(Configuration c) {
        this.config = c;
        initRegistry();
    }

    private void initRegistry() {
        if (config == null) {
            return;
        }

        try {
            initRegistry(config.getRegistry());
            initExcludes(config.getReservedDict());
        } catch (Exception e) {
            System.err.println("Warning: Initialization failed.");
        }
    }

    protected void initExcludes(final String reservedDict) throws Exception {
        Properties properties = PropertiesUtils.loadProperties(reservedDict);
        List<String> generalExcludes = StringUtils.tokenize(properties.getProperty("general"));

        Enumeration<?> extensions = properties.propertyNames();
        while (extensions.hasMoreElements()) {
            String extension  = (String) extensions.nextElement();
            if ("general".equals(extension)) {
                continue;
            }
            Parser parser = getParser(extension);
            if (parser == null) {
                System.err.println("Warning: Can not find the " + extension 
                                   + " parser, drop excludes " + properties.getProperty(extension));
                continue;
            }
            parser.getUserExcludes().addAll(StringUtils.tokenize(properties.getProperty(extension)));
            parser.getUserExcludes().addAll(generalExcludes);
        }
    }

    protected void initRegistry(final String registry) throws Exception {
        Properties properties = PropertiesUtils.loadProperties(registry);
        Enumeration<?> extensions = properties.propertyNames();
        while (extensions.hasMoreElements()) {
            String extension  = (String) extensions.nextElement();
            newParser(extension, properties.getProperty(extension));
        }
    }

    public static ParserFactory getInstance() {
        return getInstance(null);
    }

    public static ParserFactory getInstance(Configuration c) {
        if (INSTANCE == null) {
            INSTANCE = new ParserFactory(c);
        }
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
        Parser parser = registry.get(extension);
        if (parser == null) {
            return newParser(extension, "com.google.bspell.parsers." + StringUtils.capitalize(extension) + "Parser");
        }
        return parser;
    }

    public Parser newParser(final String extension, final String fullClassName) {
        try {        
            Parser parser = (Parser) Class.forName(fullClassName).newInstance();
            registry.put(extension, parser);
            return parser;
        } catch (Exception e) {
            throw new RuntimeException("We are not support file extension [" + extension 
                                       + "] yet, or it's not in the registry!");
        }
    }
}