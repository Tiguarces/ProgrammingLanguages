package com.Tiguarces.ProgrammingLanguages.scraping.task;

sealed interface Task permits WikipediaTask {
    void doScrapData();
}
