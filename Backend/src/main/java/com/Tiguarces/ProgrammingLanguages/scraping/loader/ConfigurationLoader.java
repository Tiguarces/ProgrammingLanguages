package com.Tiguarces.ProgrammingLanguages.scraping.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Configuration
@SuppressWarnings("unused")
public class ConfigurationLoader {
    public LanguageConfig.Enabled enabledLanguages;
    public LanguageConfig.Disabled disabledLanguages;

    public TaskConfig.TiobeIndex tiobeIndexTask;
    public TaskConfig.Wikipedia wikipediaTask;
    public TaskConfig.Trends trendsTask;

    public ConfigurationLoader() {
        log.info("Loading and initializing configuration file");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ConfigurationFile configurationFile = objectMapper.readValue(getFileAsString(), ConfigurationFile.class);

            LanguageConfig languageConfig = configurationFile.languages();
            this.enabledLanguages = languageConfig.enabled();
            this.disabledLanguages = languageConfig.disabled();

            TaskConfig taskConfig = configurationFile.tasks();
            this.tiobeIndexTask = taskConfig.tiobeIndex();
            this.wikipediaTask = taskConfig.wikipedia();
            this.trendsTask = taskConfig.trends();

        } catch (Exception exception) {
            log.error("Fail while loading ConfigurationFile >> Message: " + exception.getMessage());  return;

        } log.info("Loading and initializing configuration file finishing successfully");
    }

    private static String getFileAsString() throws Exception {
        return Resources.toString(Resources.getResource("configuration/Scraping.json"), UTF_8);
    }
}
