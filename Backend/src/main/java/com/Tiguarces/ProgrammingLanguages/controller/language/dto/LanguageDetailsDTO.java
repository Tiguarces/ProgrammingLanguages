package com.Tiguarces.ProgrammingLanguages.controller.language.dto;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;
import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeIndex;
import com.Tiguarces.ProgrammingLanguages.model.trend.LanguageTrend;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.Tiguarces.ProgrammingLanguages.utils.LanguageUtils.replaceToSharpSign;
import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

public record LanguageDetailsDTO(String name, String paradigms, String fileExtensions,
                                 String stableRelease, String description, String website,
                                 String implementations, String thumbnailPath, int firstAppeared,
                                 TiobeIndexStatus tiobeIndex, List<LanguageTrendDTO> trendsList) {

    private record TiobeIndexStatus(Integer rank, int status) {
        public static TiobeIndexStatus map(final TiobeIndex tiobeIndex) {
            return new TiobeIndexStatus(tiobeIndex.getRank(), tiobeIndex.getStatus().getValue());
        }
    }

    public static LanguageDetailsDTO mapToDTO(final Language language) {
        Objects.requireNonNull(language);

        var tiobeIndex = getNewestTiobeIndex(language.getTiobeIndexList());
        var doneTrends = getTrends(language.getLanguageTrends());

        return new LanguageDetailsDTO(replaceToSharpSign(language.getName()), language.getParadigms(), language.getFileExtensions(),
                                      language.getStableRelease(), language.getDescription(), language.getWebsite(), language.getImplementations(),
                                      language.getThumbnailPath(), language.getFirstAppeared(), tiobeIndex, doneTrends);
    }

    private static TiobeIndexStatus getNewestTiobeIndex(final List<TiobeIndex> languageTiobeIndexes) {
        return Objects.requireNonNull(languageTiobeIndexes)
                      .stream()
                      .min(Comparator.comparing(TiobeIndex::getIndexDate)
                                     .thenComparing(TiobeIndex::getRank))
                      .map(TiobeIndexStatus::map).orElse(null);
    }

    private static List<LanguageTrendDTO> getTrends(final List<LanguageTrend> languageTrends) {
        if(isEmpty(languageTrends)) {
            return emptyList();
        }

        return languageTrends.stream()
                .sorted(Comparator.comparingDouble(LanguageTrend::getTotalStars).reversed()
                                  .thenComparing(LanguageTrend::getMonthlyStars).reversed())
                .map(LanguageTrendDTO::mapToDTO)
                .toList();
    }
}
