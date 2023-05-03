package com.Tiguarces.ProgrammingLanguages.scraping.parser;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import static com.Tiguarces.ProgrammingLanguages.scraping.ScrapingConstants.DECIMAL_PATTERN;
import static com.Tiguarces.ProgrammingLanguages.scraping.ScrapingConstants.DECIMAL_VALUE_NOT_FOUND_EXCEPTION_MESSAGE;

sealed interface Parser <V, T extends Enum<T>, E> permits LanguageParser {
    V parse(EnumMap<T, Object> dataToParse);

    List<E> parseToEntity(List<V> doneValues);

    default String getDecimalValue(final String value) {
        Objects.requireNonNull(value);

        Matcher matcher = DECIMAL_PATTERN.matcher(value);
        if(matcher.find()) {
            return (matcher.group()).trim();
        } else throw new IllegalArgumentException(DECIMAL_VALUE_NOT_FOUND_EXCEPTION_MESSAGE + value);
    }
}
