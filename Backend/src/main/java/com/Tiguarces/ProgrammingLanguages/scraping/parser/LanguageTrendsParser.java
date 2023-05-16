package com.Tiguarces.ProgrammingLanguages.scraping.parser;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;
import com.Tiguarces.ProgrammingLanguages.model.trend.LanguageTrend;
import com.Tiguarces.ProgrammingLanguages.scraping.ScrapingUtils;
import com.Tiguarces.ProgrammingLanguages.service.language.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

import static com.Tiguarces.ProgrammingLanguages.scraping.ScrapingConstants.*;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageTrendsParser.DoneLanguageTrend;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageTrendsParser.FieldName;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageTrendsParser.FieldName.*;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@RequiredArgsConstructor
public final class LanguageTrendsParser implements Parser<DoneLanguageTrend, FieldName, LanguageTrend> {
    private final LanguageService languageService;

    @Override
    public DoneLanguageTrend parse(final EnumMap<FieldName, Object> dataToParse) {
        Objects.requireNonNull(dataToParse);

        return new DoneLanguageTrend(
                parseName((String) dataToParse.get(LANGUAGE_NAME)),
                parseField((String) dataToParse.get(REPOSITORY_NAME)),
                parseLink((String) dataToParse.get(LINK_TO_REPOSITORY)),
                parseField((String) dataToParse.get(DESCRIPTION)),
                parseStars((String) dataToParse.get(TOTAL_STARS)),
                parseStars((String) dataToParse.get(MONTHLY_STARS)),
                LocalDate.now()
        );
    }

    @Override
    public List<LanguageTrend> parseToEntity(final List<DoneLanguageTrend> trendsToParse) {
        if(isEmpty(trendsToParse)) {
            return emptyList();
        }

        LocalDate today = LocalDate.now();
        List<LanguageTrend> doneTrends = new LinkedList<>();
        Language foundLanguage = languageService.findByName(trendsToParse.get(0).languageName())
                                                .orElseThrow();

        for(var trendToParse : trendsToParse) {
            doneTrends.add(LanguageTrend.toEntity(trendToParse, today, foundLanguage));
        } foundLanguage.setLanguageTrends(doneTrends);  return doneTrends;
    }

    private static int parseStars(final String value) {
        try {
            Matcher matcher;
            if((matcher = DECIMAL_PATTERN.matcher(value)).find()) {
                String foundValue = matcher.group();

                return (foundValue.indexOf(COMMA_CHAR) != -1)
                         ? Integer.parseInt((foundValue.replaceFirst(COMMA, EMPTY)).trim())
                         : Integer.parseInt(foundValue.trim());

            } else {
                throw new IllegalArgumentException("Cannot parse number of stars to Integer >> Cause: not found digits");
            }

        } catch (Exception exception) {
            throw new IllegalArgumentException("Cannot parse number of stars to Integer >> Given value: " + value);
        }
    }

    private static final String GITHUB_LINK = "https://github.com/";
    private static String parseLink(final String link) {
        return GITHUB_LINK + ScrapingUtils.removeSpaces(link, FORWARD_SLASH);
    }

    public enum FieldName {
        REPOSITORY_NAME, LINK_TO_REPOSITORY, TOTAL_STARS,
        MONTHLY_STARS, LANGUAGE_NAME, DESCRIPTION
    }

    public record DoneLanguageTrend(String languageName, String repositoryName, String linkToRepository,
                                    String description, int totalStars, int monthlyStars, LocalDate trendsDate) {
    }
}
