package com.Tiguarces.ProgrammingLanguages.scraping.task;

import com.Tiguarces.ProgrammingLanguages.scraping.browser.BrowserClient;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.ConfigurationLoader;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.LanguageConfig;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.TaskConfig;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser.DoneLanguage;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser.FieldName;
import com.Tiguarces.ProgrammingLanguages.service.language.LanguageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static com.Tiguarces.ProgrammingLanguages.scraping.ScrapingConstants.SINGLE_LANGUAGE_TASK_LOG;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser.FieldName.*;
import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public final class WikipediaTask implements Task {

    private final LanguageParser languageParser;
    private final LanguageService languageService;

    private static final LanguageConfig.Enabled enabledLanguages = ConfigurationLoader.enabledLanguages;
    private static final TaskConfig.Wikipedia wikipediaTask = ConfigurationLoader.wikipediaTask;

    private static final List<String> defaultLanguagesList = enabledLanguages.defaultPages();
    private static final List<LanguageConfig.Enabled.CustomLanguage> customLanguagesList = enabledLanguages.customPages();

    private static final String defaultSitePattern = enabledLanguages.defaultSite();
    private static final String TASK_LOG = SINGLE_LANGUAGE_TASK_LOG.formatted("Wikipedia Task");

    @Override
    public void doScrapData() {
        log.info("Wikipedia task started");

        try(BrowserClient browserClient = new BrowserClient()) {
            int languagesAmount = (defaultLanguagesList.size() + customLanguagesList.size());
            int index = 0;

            List<DoneLanguage> languagesToSave = new ArrayList<>(defaultLanguagesList.size());

            for(String languageName: defaultLanguagesList) {
                log.info(TASK_LOG, (++index), languagesAmount, languageName);

                browserClient.loadPage(format(defaultSitePattern, languageName));
                languagesToSave.add(getLanguageFromPage(browserClient, languageName));
            }

            for(var customLanguage: customLanguagesList) {
                log.info(TASK_LOG, (++index), languagesAmount, customLanguage.name());

                browserClient.loadPage(customLanguage.site());
                languagesToSave.add(getLanguageFromPage(browserClient, customLanguage));
            }

            log.info("Start saving languages");
             languageService.saveAll(languageParser.parseToEntity(languagesToSave));
            log.info("Finishing saving languages >> Saved");

        } catch (Exception exception) {
            log.error("Wikipedia task not working properly >> Message: " + exception.getMessage());
            return;

        } log.info("Wikipedia task finished successfully");
    }

    private DoneLanguage getLanguageFromPage(final BrowserClient browserClient, final String languageName) {
        var dataToParse = new EnumMap<>(FieldName.class);
            dataToParse.put(NAME             , languageName);
            dataToParse.put(PARADIGMS        , browserClient.findElement(wikipediaTask.paradigms()));
            dataToParse.put(IMPLEMENTATIONS  , browserClient.findElement(wikipediaTask.implementations()));
            dataToParse.put(STABLE_RELEASE   , browserClient.findElement(wikipediaTask.stableRelease()));
            dataToParse.put(FILE_EXTENSIONS  , browserClient.findElement(wikipediaTask.fileExtensions()));
            dataToParse.put(DESCRIPTION      , browserClient.findElement(wikipediaTask.description()));
            dataToParse.put(WEBSITE          , browserClient.findElement(wikipediaTask.website()));
            dataToParse.put(FIRST_APPEARED   , browserClient.findElement(wikipediaTask.firstAppeared()));
            dataToParse.put(THUMBNAIL_PATH   , browserClient.findElement(wikipediaTask.thumbnailPath()));

        return languageParser.parse(dataToParse);
    }

    private DoneLanguage getLanguageFromPage(final BrowserClient browserClient, final LanguageConfig.Enabled.CustomLanguage customPage) {
        var dataToParse = new EnumMap<>(FieldName.class);
            dataToParse.put(NAME                     , customPage.name());
            dataToParse.put(PARADIGMS                , browserClient.findElement(wikipediaTask.paradigms()));
            dataToParse.put(IMPLEMENTATIONS          , browserClient.findElement(wikipediaTask.implementations()));
            dataToParse.put(STABLE_RELEASE           , browserClient.findElement(wikipediaTask.stableRelease()));
            dataToParse.put(FILE_EXTENSIONS          , browserClient.findElement(wikipediaTask.fileExtensions()));
            dataToParse.put(DESCRIPTION              , browserClient.findElement(wikipediaTask.description()));
            dataToParse.put(WEBSITE                  , browserClient.findElement(wikipediaTask.website()));
            dataToParse.put(FIRST_APPEARED           , browserClient.findElement(wikipediaTask.firstAppeared()));
            dataToParse.put(THUMBNAIL_PATH           , browserClient.findElement(wikipediaTask.thumbnailPath()));
            dataToParse.put(CUSTOM_PAGE              , customPage);

        return languageParser.parse(dataToParse);
    }
}
