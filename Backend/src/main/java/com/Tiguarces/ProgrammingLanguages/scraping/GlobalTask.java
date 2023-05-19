package com.Tiguarces.ProgrammingLanguages.scraping;

import com.Tiguarces.ProgrammingLanguages.scraping.task.LanguageTrendsTask;
import com.Tiguarces.ProgrammingLanguages.scraping.task.TiobeIndexTask;
import com.Tiguarces.ProgrammingLanguages.scraping.task.WikipediaTask;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class GlobalTask {
    private final WikipediaTask wikipediaTask;
    private final TiobeIndexTask tiobeIndexTask;
    private final LanguageTrendsTask trendsTask;

    @Scheduled(cron = "${custom.task.cron}")
    public void initTasks() {
        wikipediaTask.doScrapData();
        tiobeIndexTask.doScrapData();
        trendsTask.doScrapData();
    }
}
