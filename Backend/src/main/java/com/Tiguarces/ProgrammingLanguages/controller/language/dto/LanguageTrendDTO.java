package com.Tiguarces.ProgrammingLanguages.controller.language.dto;

import com.Tiguarces.ProgrammingLanguages.model.trend.LanguageTrend;

public record LanguageTrendDTO(String repositoryName, String linkToRepository,
                               String description, double totalStars, double monthlyStars) {

    public static LanguageTrendDTO mapToDTO(final LanguageTrend languageTrend) {
        return new LanguageTrendDTO(languageTrend.getRepositoryName(), languageTrend.getLinkToRepository(),
                                    languageTrend.getDescription(), languageTrend.getTotalStars(), languageTrend.getMonthlyStars());
    }
}
