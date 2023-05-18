package com.Tiguarces.ProgrammingLanguages.scraping.loader;

import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeStatus;

import java.util.EnumMap;
import java.util.Map;

public record TaskConfig(TiobeIndex tiobeIndex, Wikipedia wikipedia, Trends trends) {

    public record TiobeIndex(String site, Scraping scraping) {
        public record Scraping(String mainLanguagesBox, String otherLanguagesBox,
                               String singleRow, EnumMap<TiobeStatus, String> status, FieldPositions fieldPositions) { }

        public record FieldPositions(EnumMap<FieldPosition, Short> mainLanguages,
                                     EnumMap<FieldPosition, Short> otherLanguages) {
            public enum FieldPosition {
                POSITION, CHANGE_ICON, LANGUAGE_NAME
            }
        }
    }

    public record Wikipedia(String thumbnailPath, String paradigms, String implementations,
                            String firstAppeared, String stableRelease, String fileExtensions,
                            String website, String description) { }

    public record Trends(String site, Scraping scraping) {
        public record Scraping(String repositoriesBox, SingleRepository singleRepository,
                               Map<String, String> customLanguagePhrases) {
            public record SingleRepository(String box, String name, String description,
                                           String link, String totalStars, String monthlyStars) { }
        }
    }
}
