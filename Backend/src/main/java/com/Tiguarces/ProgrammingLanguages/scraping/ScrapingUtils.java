package com.Tiguarces.ProgrammingLanguages.scraping;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;

public final class ScrapingUtils {
    private ScrapingUtils() { }

    public static String removeSpaces(final String value, final String delimiter) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(delimiter);

        StringBuilder valueBuilder = new StringBuilder();
        String[] splitValue = splitValue(value, delimiter);
        int splitSize = splitValue.length;

        for (int i = 0; i < splitSize; i++) {
            valueBuilder.append(splitValue[i].trim());

            if(i < (splitSize - 1)) {
                valueBuilder.append(delimiter);
            }
        } return valueBuilder.toString();
    }

    public static String[] splitValue(final String value, final String regex) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(regex);

        return Arrays.stream(value.split(regex))
                      .map(String::trim)
                      .filter(StringUtils::isNotBlank)
                     .toArray(String[]::new);
    }
}
