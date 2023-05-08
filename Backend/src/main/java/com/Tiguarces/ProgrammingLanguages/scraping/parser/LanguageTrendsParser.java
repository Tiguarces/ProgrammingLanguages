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
        List<LanguageTrend> doneTrends = new LinkedList<>();

        LocalDate today = LocalDate.now();
        Language foundLanguage;

        for(var trendToParse : Objects.requireNonNull(trendsToParse)) {
            foundLanguage = languageService.findByName(trendToParse.languageName())
                                           .orElseThrow();

            doneTrends.add(LanguageTrend.toEntity(trendToParse, today, foundLanguage));
        } return doneTrends;
    }

    private static double parseStars(final String value) {
        try {
            Matcher matcher;
            if((matcher = DECIMAL_PATTERN.matcher(value)).find()) {
                String foundValue = matcher.group();

                return (foundValue.indexOf(COMMA_CHAR) != -1)
                         ? Double.parseDouble((foundValue.replace(COMMA_CHAR, DOT_CHAR)).trim())
                         : Double.parseDouble(foundValue.trim());

            } else {
                throw new IllegalArgumentException("Cannot parse number of stars to Integer >> Cause: not found digits");
            }

        } catch (Exception exception) {
            throw new IllegalArgumentException("Cannot parse number of stars to Integer >> Given value: " + value);
        }
    }

    private static final String GITHUB_LINK = "https://github.com/";
    private static String parseLink(final String link) {
        return GITHUB_LINK + ScrapingUtils.removeSpaces(link, "/");
    }

    public enum FieldName {
        REPOSITORY_NAME, LINK_TO_REPOSITORY, TOTAL_STARS,
        MONTHLY_STARS, LANGUAGE_NAME, DESCRIPTION
    }

    public record DoneLanguageTrend(String languageName, String repositoryName, String linkToRepository,
                                    String description, double totalStars, double monthlyStars, LocalDate trendsDate) {
    }
}
