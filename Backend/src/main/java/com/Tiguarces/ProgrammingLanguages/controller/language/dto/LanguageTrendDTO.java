package com.Tiguarces.ProgrammingLanguages.controller.language.dto;

import com.Tiguarces.ProgrammingLanguages.model.trend.LanguageTrend;

public record LanguageTrendDTO(String repositoryName, String linkToRepository,
                               double totalStars, double monthlyStars) {

    public static LanguageTrendDTO mapToDTO(final LanguageTrend languageTrend) {
        return new LanguageTrendDTO(languageTrend.getRepositoryName(), languageTrend.getLinkToRepository(),
                                    languageTrend.getTotalStars(), languageTrend.getMonthlyStars());
    }
}
