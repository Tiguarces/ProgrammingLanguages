package com.Tiguarces.ProgrammingLanguages.scraping.parser;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.LanguageConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import static com.Tiguarces.ProgrammingLanguages.scraping.ScrapingConstants.*;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser.DoneLanguage;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser.FieldName;
import static org.apache.commons.lang3.StringUtils.*;

@Component
public final class LanguageParser implements Parser<DoneLanguage, FieldName, Language> {

    @Override
    public DoneLanguage parse(final EnumMap<FieldName, Object> dataToParse) {
        Objects.requireNonNull(dataToParse);

        return new DoneLanguage(
                parseName((String) dataToParse.get(FieldName.NAME)),
                parseDate((String) dataToParse.get(FieldName.FIRST_APPEARED)),
                parseNullableField((String) dataToParse.get(FieldName.PARADIGMS)),
                parseNullableField((String) dataToParse.get(FieldName.IMPLEMENTATIONS)),
                parseStableRelease((String) dataToParse.get(FieldName.STABLE_RELEASE)),
                parseNullableField((String) dataToParse.get(FieldName.FILE_EXTENSIONS)),
                parseNullableField((String) dataToParse.get(FieldName.DESCRIPTION)),
                parseNullableField((String) dataToParse.get(FieldName.WEBSITE)),

                parseThumbnailPath((String) dataToParse.get(FieldName.THUMBNAIL_PATH),
                                            dataToParse.get(FieldName.OPTIONAL_THUMBNAIL_PATH))
        );
    }

    @Override
    public List<Language> parseToEntity(final List<DoneLanguage> doneValues) {
        return doneValues.stream()
                 .map(Language::toEntity)
                .toList();
    }

    private String parseName(final String value) {
        return WordUtils.capitalize(getValueOrThrow(value, FieldName.NAME).toLowerCase());
    }

    private String parseNullableField(final String value) {
        return (isNotBlank(value))
                ? value.replaceAll(WIKIPEDIA_TEXT_REGEX_REPLACE, EMPTY) : null;
    }

    private String getValueOrThrow(final String value, final FieldName fieldName) {
        if(isBlank(value)) {
            throw new IllegalArgumentException(PARSING_LANGUAGE_EXCEPTION_MESSAGE + fieldName.name());
        }

        return value.replaceAll(WIKIPEDIA_TEXT_REGEX_REPLACE, EMPTY);
    }

    private String parseStableRelease(String stableRelease) {
        if(isBlank(stableRelease)) {
            return null; // Permit nullable value
        }

        stableRelease = getValueOrThrow(stableRelease, FieldName.STABLE_RELEASE);

        if(stableRelease.indexOf(SEMICOLON_CHAR) != -1) {
            stableRelease = stableRelease.split(SEMICOLON)[0];
        } return stableRelease;
    }

    private LocalDate parseDate(String date) {
        date = getValueOrThrow(date, FieldName.FIRST_APPEARED);

        Matcher matcher;
        if((matcher = DATE_PATTERN.matcher(date)).find()) {
            String matchedFragment = (matcher.group()).replaceFirst(DATE_YEARS_AGO_REGEX, EMPTY).trim();

            if(matchedFragment.indexOf(s_CHAR) != -1) {
                int years = Integer.parseInt(getDecimalValue(matchedFragment));
                int differenceBetweenDates = LocalDate.now().minusYears(years).getYear();
                return LocalDate.now().minusYears(differenceBetweenDates);
            }

            int years = Integer.parseInt(matchedFragment);
            return LocalDate.now().minusYears(years);

        } else {
            throw new IllegalArgumentException(PARSING_DATE_LANGUAGE_EXCEPTION_MESSAGE + date);
        }
    }

    private String parseThumbnailPath(final String thumbnailPath, final Object customPage) {
        if(customPage instanceof LanguageConfig.Enabled.CustomLanguage otherLanguage) {
            return (isBlank(thumbnailPath))
                     ? otherLanguage.thumbnailPath() : thumbnailPath;
        } else return thumbnailPath;
    }

    public enum FieldName {
        NAME, FIRST_APPEARED, DESCRIPTION, PARADIGMS, IMPLEMENTATIONS,
        FILE_EXTENSIONS, WEBSITE, THUMBNAIL_PATH, STABLE_RELEASE,
        OPTIONAL_THUMBNAIL_PATH
    }

    @Getter
    public static final class DoneLanguage {
        private final String name;
        private final String paradigms;
        private final String implementations;
        private final String stableRelease;
        private final String fileExtensions;
        private final String description;
        private final String website;
        private final int firstAppeared;

        @Setter private String thumbnailPath;

        public DoneLanguage(final String name, final LocalDate firstAppeared,
                            final String paradigms, final String implementations,
                            final String stableRelease, final String fileExtensions,
                            final String description, final String website, final String thumbnailPath) {

            this.name = name;
            this.paradigms = paradigms;
            this.implementations = implementations;
            this.stableRelease = stableRelease;
            this.fileExtensions = fileExtensions;
            this.description = description;
            this.website = website;
            this.thumbnailPath = thumbnailPath;

            Objects.requireNonNull(firstAppeared);
            this.firstAppeared = (firstAppeared.getYear());
        }
    }
}
