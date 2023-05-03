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
    public static LanguageConfig.Enabled enabledLanguages;
    public static LanguageConfig.Disabled disabledLanguages;

    public static TaskConfig.TiobeIndex tiobeIndexTask;
    public static TaskConfig.Wikipedia wikipediaTask;
    public static TaskConfig.Trends trendsTask;

    public ConfigurationLoader() {
        log.info("Loading and initializing configuration file");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ConfigurationFile configurationFile = objectMapper.readValue(getFileAsString(), ConfigurationFile.class);

            LanguageConfig languageConfig = configurationFile.languages();
            ConfigurationLoader.enabledLanguages = languageConfig.enabled();
            ConfigurationLoader.disabledLanguages = languageConfig.disabled();

            TaskConfig taskConfig = configurationFile.tasks();
            ConfigurationLoader.tiobeIndexTask = taskConfig.tiobeIndex();
            ConfigurationLoader.wikipediaTask = taskConfig.wikipedia();
            ConfigurationLoader.trendsTask = taskConfig.trends();

        } catch (Exception exception) {
            log.error("Fail while loading ConfigurationFile >> Message: " + exception.getMessage());  return;

        } log.info("Loading and initializing configuration file finishing successfully");
    }

    private static String getFileAsString() throws Exception {
        return Resources.toString(Resources.getResource("configuration/Scraping.json"), UTF_8);
    }
}
