package com.Tiguarces.ProgrammingLanguages.scraping.task;

import com.Tiguarces.ProgrammingLanguages.scraping.browser.BrowserClient;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.ConfigurationLoader;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.LanguageConfig;
import com.Tiguarces.ProgrammingLanguages.scraping.loader.TaskConfig;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageTrendsParser;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageTrendsParser.DoneLanguageTrend;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageTrendsParser.FieldName;
import com.Tiguarces.ProgrammingLanguages.service.trends.LanguageTrendsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.Tiguarces.ProgrammingLanguages.scraping.ScrapingConstants.SINGLE_LANGUAGE_TASK_LOG;
import static com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageTrendsParser.FieldName.*;
import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public final class LanguageTrendsTask implements Task {
    private final LanguageTrendsParser trendsParser;
    private final LanguageTrendsService trendsService;

    private static final TaskConfig.Trends trendsTask = ConfigurationLoader.trendsTask;
    private static final TaskConfig.Trends.Scraping trendsScraping = trendsTask.scraping();
    private static final TaskConfig.Trends.Scraping.SingleRepository singleRepository = trendsScraping.singleRepository();
    private static final Map<String, String> customLanguagesPhrases = trendsScraping.customLanguagePhrases();

    private static final LanguageConfig.Enabled enabledLanguages = ConfigurationLoader.enabledLanguages;
    private static final List<String> defaultLanguagesList = enabledLanguages.defaultPages();
    private static final List<LanguageConfig.Enabled.CustomLanguage> customLanguagesList = enabledLanguages.customPages();

    private static final String TASK_LOG = SINGLE_LANGUAGE_TASK_LOG.formatted("LanguageTrends task");
    private static final String REPOSITORY_BOX = trendsScraping.repositoriesBox();

    @Override
    public void doScrapData() {
        log.info("LanguageTrends task started");

        try(BrowserClient browserClient = new BrowserClient()) {
            int languagesAmount = (defaultLanguagesList.size() + customLanguagesList.size());
            Map<String, List<DoneLanguageTrend>> trendsToSave = new HashMap<>(languagesAmount);

            int index = 0;
            for(String languageName: enabledLanguages.defaultPages()) {
                log.info(TASK_LOG, (++index), languagesAmount, languageName);

                if(customLanguagesPhrases.containsKey(languageName)) {
                    languageName = customLanguagesPhrases.get(languageName);
                }

                browserClient.loadPage(format(trendsTask.site(), languageName));
                trendsToSave.put(languageName, getTrendsFromPage(browserClient, languageName));
            }

            String languageName;
            for(var customPage: enabledLanguages.customPages()) {
                log.info(TASK_LOG, (++index), languagesAmount, (languageName = customPage.name()));

                browserClient.loadPage(format(trendsTask.site(), customLanguagesPhrases.getOrDefault(languageName, languageName)));
                trendsToSave.put(languageName, getTrendsFromPage(browserClient, languageName));
            }

            log.info("Start saving languages trends");
            for(var languageTrends: trendsToSave.entrySet()) {
                trendsService.saveAll(trendsParser.parseToEntity(languageTrends.getValue()));
            }
            log.info("Finishing saving languages trends >> Saved");

        } catch (Exception exception) {
            log.error("LanguageTrends task not working properly >> Message: " + exception.getMessage());
            return;

        } log.info("LanguageTrends task finished successfully");
    }

    private List<DoneLanguageTrend> getTrendsFromPage(final BrowserClient browserClient, final String languageName) {
        List<DoneLanguageTrend> trends = new ArrayList<>();

        for(var singleTrend: browserClient.findElements(REPOSITORY_BOX + singleRepository.box())) {
            var dataToParse = new EnumMap<>(FieldName.class);
                dataToParse.put(LANGUAGE_NAME       , languageName);
                dataToParse.put(REPOSITORY_NAME     , browserClient.selectElementAndGetValue(singleTrend, singleRepository.name()));
                dataToParse.put(LINK_TO_REPOSITORY  , browserClient.selectElementAndGetValue(singleTrend, singleRepository.link()));
                dataToParse.put(TOTAL_STARS         , browserClient.selectElementAndGetValue(singleTrend, singleRepository.totalStars()));
                dataToParse.put(MONTHLY_STARS       , browserClient.selectElementAndGetValue(singleTrend, singleRepository.monthlyStars()));
                dataToParse.put(DESCRIPTION         , browserClient.selectElementAndGetValue(singleTrend, singleRepository.description()));

            if(dataToParse.get(MONTHLY_STARS) == null || dataToParse.get(TOTAL_STARS) == null) {
                continue; // Skip this repository
            }

            trends.add(trendsParser.parse(dataToParse));

        } return trends.stream()
                       .sorted(Comparator.comparingDouble(DoneLanguageTrend::totalStars).reversed()
                                         .thenComparing(Comparator.comparingDouble(DoneLanguageTrend::monthlyStars).reversed()))
                       .limit(5)
                       .toList();
    }
}
