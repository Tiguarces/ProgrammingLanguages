package com.Tiguarces.ProgrammingLanguages.controller.language.dto;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;

import java.util.List;
import java.util.Objects;

import static com.Tiguarces.ProgrammingLanguages.utils.LanguageUtils.replaceToSharpSign;

public record LanguageDetailsDTO(String name, String paradigms, String fileExtensions,
                                 String stableRelease, String description, String website,
                                 String implementations, String thumbnailPath, int firstAppeared,
                                 List<TiobeIndexDTO> tiobeList, List<LanguageTrendDTO> trendsList) {

    public static LanguageDetailsDTO mapToDTO(final Language language) {
        Objects.requireNonNull(language);

        var mappedTiobeList = (language.getTiobeIndexList()).stream()
                                                    .map(TiobeIndexDTO::mapToDTO)
                                                    .toList();

        var mappedTrendsList = (language.getLanguageTrends()).stream()
                                                    .map(LanguageTrendDTO::mapToDTO)
                                                    .toList();

        return new LanguageDetailsDTO(replaceToSharpSign(language.getName()), language.getParadigms(), language.getFileExtensions(),
                                      language.getStableRelease(), language.getDescription(), language.getWebsite(), language.getImplementations(),
                                      language.getThumbnailPath(), language.getFirstAppeared(), mappedTiobeList, mappedTrendsList);
    }
}
