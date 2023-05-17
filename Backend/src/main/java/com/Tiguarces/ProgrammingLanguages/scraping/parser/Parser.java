package com.Tiguarces.ProgrammingLanguages.scraping.parser;

import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser.FieldName;
import org.apache.commons.text.WordUtils;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import static com.Tiguarces.ProgrammingLanguages.scraping.ScrapingConstants.*;
import static org.apache.commons.lang3.StringUtils.*;

sealed interface Parser <V, T extends Enum<T>, E> permits LanguageParser, LanguageTrendsParser, TiobeIndexParser {
    V parse(EnumMap<T, Object> dataToParse);

    List<E> parseToEntity(List<V> doneValues);

    default String getDecimalValue(final String value) {
        Objects.requireNonNull(value);

        Matcher matcher = DECIMAL_PATTERN.matcher(value);
        if(matcher.find()) {
            return (matcher.group()).trim();
        } else throw new IllegalArgumentException(DECIMAL_VALUE_NOT_FOUND_EXCEPTION_MESSAGE + value);
    }

    default String parseName(String value) {
        value = getValueOrThrow(value, FieldName.NAME);

        // %23 is #, so languages like: C# and F#
        if(value.contains("%23")) {
            value = value.replaceFirst("%23", "_Sharp");
        }

        if(containsIgnoreCase(value,".net")) {
            value = value.replaceFirst("[(]\\.[nN][eE][tT][)]", EMPTY).trim();
        }

        return WordUtils.capitalize(getValueOrThrow(value, FieldName.NAME).toLowerCase());
    }

    default String parseField(final String value) {
        return (isNotBlank(value))
                ? value.replaceAll(WIKIPEDIA_TEXT_REGEX_REPLACE, EMPTY) : null;
    }

    default String getValueOrThrow(final String value, final FieldName fieldName) {
        if(isBlank(value)) {
            throw new IllegalArgumentException(PARSING_LANGUAGE_EXCEPTION_MESSAGE + fieldName.name());
        } return value;
    }
}
