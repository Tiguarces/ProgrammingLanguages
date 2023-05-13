package com.Tiguarces.ProgrammingLanguages.utils;

import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class LanguageUtils {
    private LanguageUtils() { }

    public static final char SHARP_CHAR = '#';

    public static final String SHARP = "#";
    public static final String SHARP_WORD = "_sharp";

    @Getter
    public static final class Patterns {
        private static final Pattern DECIMAL_PATTERN = Pattern.compile("^(\\d+|\\d+[,.]\\d+)$");
    }

    public static String replaceToSharpWord(final String name) {
        Objects.requireNonNull(name);
        return (name.indexOf(SHARP_CHAR) != -1)
                 ? name.replaceFirst(SHARP, SHARP_WORD) : name;
    }

    public static String replaceToSharpSign(final String name) {
        return (containsIgnoreCase(name, SHARP_WORD))
                 ? name.replaceFirst(SHARP_WORD, SHARP) : name;
    }

    public static String validLanguageName(final String languageName) throws IllegalArgumentException {
        if(isBlank(languageName) && (Patterns.DECIMAL_PATTERN.matcher(languageName).matches())) {
            throw new IllegalArgumentException("Language name is not valid >> Is blank or contains digits");
        } else return languageName;
    }
}
