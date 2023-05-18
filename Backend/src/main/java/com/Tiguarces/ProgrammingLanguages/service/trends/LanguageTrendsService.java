package com.Tiguarces.ProgrammingLanguages.service.trends;

import com.Tiguarces.ProgrammingLanguages.model.trend.LanguageTrend;
import com.Tiguarces.ProgrammingLanguages.repository.trends.LanguageTrendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageTrendsService implements ServiceTemplate {
    private final LanguageTrendRepository trendRepository;

    @Override
    public void saveAll(final List<LanguageTrend> doneTrends) {
        Objects.requireNonNull(doneTrends);

        Optional<LanguageTrend> foundTrend;
        for(var trend: doneTrends) {
            if((foundTrend = trendRepository.findByRepositoryName(trend.getRepositoryName())).isPresent()) {
                checkTrend(foundTrend.get(), trend);
            } else {
                trendRepository.save(trend);
            }
        }
    }

    private void checkTrend(final LanguageTrend foundTrend, final LanguageTrend trendToSave) {
        if(!foundTrend.equals(trendToSave)) { // Then update data
            foundTrend.setTrendsDate(trendToSave.getTrendsDate());
            foundTrend.setLinkToRepository(trendToSave.getLinkToRepository());
            foundTrend.setTotalStars(trendToSave.getTotalStars());
            foundTrend.setMonthlyStars(trendToSave.getMonthlyStars());
            foundTrend.setDescription(trendToSave.getDescription());
        }
    }
}
