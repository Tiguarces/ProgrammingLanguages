package com.Tiguarces.ProgrammingLanguages.scraping.task;

import com.Tiguarces.ProgrammingLanguages.scraping.browser.BrowserClient;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.ConfigurationLoader;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.LanguageConfig;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.LanguageConfig.Enabled.CustomLanguage;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.TaskConfig;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.TaskConfig.TiobeIndex.FieldPositions.FieldPosition;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.TiobeIndexParser;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.TiobeIndexParser.DoneTiobeIndex;
import com.Tiguarces.ProgrammingLanguages.service.tiobe.TiobeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static com.Tiguarces.ProgrammingLanguages.scraping.ScrapingConstants.SRC_ATTR;
import static com.Tiguarces.ProgrammingLanguages.scraping.loader.TaskConfig.TiobeIndex.FieldPositions.FieldPosition.*;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.TiobeIndexParser.FieldName;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.TiobeIndexParser.FieldName.*;
import static com.Tiguarces.ProgrammingLanguages.utils.LanguageUtils.replaceToSharpWord;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

@Slf4j
@Component
@RequiredArgsConstructor
public final class TiobeIndexTask implements Task {

    private final TiobeService tiobeService;
    private final TiobeIndexParser tiobeParser;

    private static final LanguageConfig.Enabled enabledLanguages = ConfigurationLoader.enabledLanguages;
    private static final TaskConfig.TiobeIndex tiobeIndexTask = ConfigurationLoader.tiobeIndexTask;
    private static final TaskConfig.TiobeIndex.Scraping scraping = tiobeIndexTask.scraping();
    private static final TaskConfig.TiobeIndex.FieldPositions fieldPositions = scraping.fieldPositions();

    private static final List<String> defaultLanguagesList = enabledLanguages.defaultPages();
    private static final List<String> customLanguagesList = (enabledLanguages.customPages())
                                                                             .stream().map(CustomLanguage::name).toList();

    @Override
    public void doScrapData() {
        log.info("TiobeIndex task started");

        try(BrowserClient browserClient = new BrowserClient()) {
            List<DoneTiobeIndex> doneIndexes = new ArrayList<>(defaultLanguagesList.size());

            browserClient.loadPage(tiobeIndexTask.site());
            List<Element> mainLanguageIndexes = browserClient.findElements(scraping.mainLanguagesBox());
            List<Element> otherLanguageIndexes = browserClient.findElements(scraping.otherLanguagesBox());

            var mainLanguagePositions = fieldPositions.mainLanguages();
            var otherLanguagePositions = fieldPositions.otherLanguages();

            scrapLanguages(doneIndexes,  mainLanguageIndexes , mainLanguagePositions);
            scrapLanguages(doneIndexes,  otherLanguageIndexes, otherLanguagePositions);

            log.info("Start saving tiobe indexes");
            tiobeService.saveAll(tiobeParser.parseToEntity(doneIndexes));
            log.info("Finishing saving tiobe indexes >> Saved");

        } catch (Exception exception) {
            log.error("TiobeIndex task not working properly >> Message: " + exception.getMessage());
            return;

        } log.info("TiobeIndex task finished successfully");
    }

    private void scrapLanguages(final List<DoneTiobeIndex> doneIndexes,
                                final List<Element> languageRows,
                                final EnumMap<FieldPosition, Short> positions) {

        Elements singleRow;
        String currentLanguageName;
        LocalDate today = LocalDate.now();

        for(var singleIndex: languageRows) {
            for(var row: singleIndex.select(scraping.singleRow())) {
                singleRow = row.select("td");

                var dataToParse = new EnumMap<>(FieldName.class);
                    dataToParse.put(INDEX_DATE     ,  today);
                    dataToParse.put(RANK           ,  singleRow.get(positions.get(POSITION)).text());
                    dataToParse.put(NAME           ,  (currentLanguageName = singleRow.get(positions.get(LANGUAGE_NAME)).text()));

                if(positions.containsKey(CHANGE_ICON)) {
                    dataToParse.put(TIOBE_STATUS, singleRow.get(positions.get(CHANGE_ICON))
                                                           .select("> img").attr(SRC_ATTR));
                } else {
                    dataToParse.put(TIOBE_STATUS, null); // Then will be neutral status
                }

                if (containsLanguage(defaultLanguagesList, currentLanguageName) || containsLanguage(customLanguagesList, currentLanguageName)) {
                    doneIndexes.add(tiobeParser.parse(dataToParse));
                }
            }
        }
    }

    private static boolean containsLanguage(final List<String> languageNames, final String desiredName) {
        return languageNames.stream()
                            .anyMatch(languageName -> containsIgnoreCase(languageName, replaceToSharpWord(desiredName)));
    }
}
