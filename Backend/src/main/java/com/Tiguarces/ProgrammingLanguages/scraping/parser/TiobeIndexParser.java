package com.Tiguarces.ProgrammingLanguages.scraping.parser;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;
import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeIndex;
import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeStatus;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.ConfigurationLoader;
import com.Tiguarces.ProgrammingLanguages.service.language.LanguageService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.Tiguarces.ProgrammingLanguages.scraping.parser.TiobeIndexParser.DoneTiobeIndex;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.TiobeIndexParser.FieldName;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.TiobeIndexParser.FieldName.*;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public final class TiobeIndexParser implements Parser<DoneTiobeIndex, FieldName, TiobeIndex> {

    private final LanguageService languageService;
    private final EnumMap<TiobeStatus, String> statusMap;

    public TiobeIndexParser(final LanguageService languageService, final ConfigurationLoader configurationLoader) {
        this.languageService = languageService;
        this.statusMap = configurationLoader.tiobeIndexTask
                                            .scraping().status();
    }

    @Override
    public DoneTiobeIndex parse(final EnumMap<FieldName, Object> dataToParse) {
        Objects.requireNonNull(dataToParse);

        return new DoneTiobeIndex(
                parseName((String) dataToParse.get(NAME)),
                parsePosition((String) dataToParse.get(RANK)),
                parseIndexDate(dataToParse.get(INDEX_DATE)),
                getStatus(dataToParse.get(TIOBE_STATUS))
        );
    }

    @Override
    public List<TiobeIndex> parseToEntity(final List<DoneTiobeIndex> doneTiobeIndexes) {
        if(isEmpty(doneTiobeIndexes)) {
            return emptyList();
        }

        LocalDate today = LocalDate.now();
        List<TiobeIndex> parsedTiobeIndexes = new LinkedList<>();
        Language foundLanguage;

        for(var tiobeIndexToParse : doneTiobeIndexes) {
            foundLanguage  = languageService.findByName(tiobeIndexToParse.languageName())
                                            .orElseThrow();

            parsedTiobeIndexes.add(TiobeIndex.toEntity(tiobeIndexToParse, today, foundLanguage));
            foundLanguage.setTiobeIndexList(parsedTiobeIndexes);

        } return parsedTiobeIndexes;
    }

    private static LocalDate parseIndexDate(final Object localDate) {
        try {
            return (LocalDate) localDate;
        } catch (Exception exception) {
            return LocalDate.now();
        }
    }

    private static Integer parsePosition(final String position) {
        try {
            return Integer.parseInt(position.trim());
        } catch (Exception exception) {
            return null;
        }
    }

    public enum FieldName {
        RANK, INDEX_DATE, TIOBE_STATUS, NAME
    }


    private TiobeStatus getStatus(final Object value) {
        try {
            String parsedValue = value.toString();

            for(var entry: statusMap.entrySet()) {
                if(containsIgnoreCase(parsedValue, entry.getValue())) {
                    return entry.getKey();
                }
            }

        } catch (Exception ignored) {
        } return TiobeStatus.NEUTRAL;
    }

    public record DoneTiobeIndex(String languageName, Integer rank, LocalDate indexDate, TiobeStatus status) { }
}
