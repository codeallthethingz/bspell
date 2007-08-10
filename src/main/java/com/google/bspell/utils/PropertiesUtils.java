package com.google.bspell.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtils {
    private PropertiesUtils() {
    }

    public static Properties loadProperties(final String fileName) throws IOException, FileNotFoundException {
        Properties properties = new Properties();
        properties.load(new BufferedInputStream(new FileInputStream(fileName)));
        return properties;
    }
}