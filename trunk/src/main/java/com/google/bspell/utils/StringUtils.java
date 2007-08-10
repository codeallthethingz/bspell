package com.google.bspell.utils;

public final class StringUtils {
    private StringUtils() {
    }

    public static String capitalize(String s) {
        char[] chars = s.toCharArray();
        if (chars[0] > 'a' && chars[0] < 'z') {
            chars[0] = (char) (chars[0] - 32);
        }
        return new String(chars);
    }
}