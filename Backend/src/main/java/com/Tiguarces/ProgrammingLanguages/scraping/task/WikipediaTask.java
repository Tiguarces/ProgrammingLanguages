package com.Tiguarces.ProgrammingLanguages.scraping.task;

import com.Tiguarces.ProgrammingLanguages.scraping.browser.BrowserClient;
import com.Tiguarces.ProgrammingLanguages.scraping.browser.BrowserConstants;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.ConfigurationLoader;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.LanguageConfig;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.TaskConfig;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser.DoneLanguage;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser.FieldName;
import com.Tiguarces.ProgrammingLanguages.service.language.LanguageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static com.Tiguarces.ProgrammingLanguages.scraping.ScrapingConstants.SINGLE_LANGUAGE_TASK_LOG;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser.FieldName.*;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.*;

@Slf4j
@Component
public final class WikipediaTask implements Task {
    private final LanguageParser languageParser;
    private final LanguageService languageService;

    public WikipediaTask(final LanguageParser languageParser, final LanguageService languageService,
                         final ConfigurationLoader configurationLoader, final BrowserConstants browserConstants) {

        this.languageParser = languageParser;
        this.languageService = languageService;

        var enabledLanguages = configurationLoader.enabledLanguages;
        this.wikipediaTask = configurationLoader.wikipediaTask;
        this.defaultLanguagesList = enabledLanguages.defaultPages();
        this.customLanguagesList = enabledLanguages.customPages();
        this.defaultSitePattern = enabledLanguages.defaultSite();

        this.browserConstants = browserConstants;
    }

    private final TaskConfig.Wikipedia wikipediaTask;

    private final List<String> defaultLanguagesList;
    private final List<LanguageConfig.Enabled.CustomLanguage> customLanguagesList;

    private final String defaultSitePattern;
    private final BrowserConstants browserConstants;
    private final String TASK_LOG = SINGLE_LANGUAGE_TASK_LOG.formatted("Wikipedia Task");

    @Override
    public void doScrapData() {
        log.info("Wikipedia task started");

        try(BrowserClient browserClient = new BrowserClient(browserConstants)) {
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

                if(isBlank(customLanguage.site())) {
                    browserClient.loadPage(format(defaultSitePattern, customLanguage.name()));
                } else {
                    browserClient.loadPage(customLanguage.site());
                }

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
            dataToParse.put(DESCRIPTION      , browserClient.findWikipediaDescription(wikipediaTask.description()));
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

        if(isNotBlank(customPage.thumbnailPath()) && !equalsIgnoreCase(customPage.thumbnailPath(), (String) dataToParse.get(THUMBNAIL_PATH))) {
            dataToParse.put(THUMBNAIL_PATH, customPage.thumbnailPath());
        }

        if(isNotBlank(customPage.website()) && !equalsIgnoreCase(customPage.website(), (String) dataToParse.get(WEBSITE))) {
            dataToParse.put(WEBSITE, customPage.website());
        }

        return languageParser.parse(dataToParse);
    }
}
