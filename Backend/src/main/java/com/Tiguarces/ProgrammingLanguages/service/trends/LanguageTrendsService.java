package com.Tiguarces.ProgrammingLanguages.service.trends;

import com.Tiguarces.ProgrammingLanguages.model.trend.LanguageTrend;
import com.Tiguarces.ProgrammingLanguages.repository.trends.LanguageTrendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageTrendsService implements ServiceTemplate {
    private final LanguageTrendRepository trendRepository;

    @Override
    public void saveAll(final List<LanguageTrend> doneTrends) {
        Objects.requireNonNull(doneTrends);
        trendRepository.saveAll(doneTrends);
    }
}
