package com.Tiguarces.ProgrammingLanguages.scraping.task;

@SuppressWarnings("unused")
sealed interface Task permits WikipediaTask, LanguageTrendsTask, TiobeIndexTask {
    void doScrapData();
}
