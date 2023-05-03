package com.Tiguarces.ProgrammingLanguages.scraping.loader;

import java.util.List;

public record LanguageConfig(Enabled enabled, Disabled disabled) {

    public record Enabled(String defaultSite, List<String> defaultPages, List<CustomLanguage> customPages) {
        public record CustomLanguage(String name, String site, String thumbnailPath) { }
    }

    public record Disabled(List<String> languages) { }
}
