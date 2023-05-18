package com.Tiguarces.ProgrammingLanguages.model.trend;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageTrendsParser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LanguageTrend")
public class LanguageTrend {

    @Id
    @Column(name = "TrendId")
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(name = "RepositoryName", nullable = false, unique = true)
    private String repositoryName;

    @Column(name = "LinkToRepository", nullable = false, unique = true)
    private String linkToRepository;

    @Column(name = "TotalStars", nullable = false)
    private int totalStars;

    @Column(name = "MonthlyStars", nullable = false)
    private int monthlyStars;

    @Column(name = "Description")
    private String description;

    @Column(name = "TrendsDate", nullable = false)
    private LocalDate trendsDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LanguageId")
    private Language language;

    public static LanguageTrend toEntity(final LanguageTrendsParser.DoneLanguageTrend doneLanguageTrend, final LocalDate trendDate, final Language language) {
        return new LanguageTrend(null,
                doneLanguageTrend.repositoryName(),
                doneLanguageTrend.linkToRepository(),
                doneLanguageTrend.totalStars(),
                doneLanguageTrend.monthlyStars(),
                doneLanguageTrend.description(),
                trendDate,
                language
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LanguageTrend trend = (LanguageTrend) o;
        return Objects.equals(repositoryName, trend.repositoryName)         &&
               Objects.equals(linkToRepository, trend.linkToRepository)     &&
               Objects.equals(description, trend.description)               &&
               Objects.equals(trendsDate, trend.trendsDate)                 &&
               totalStars == trend.totalStars                               &&
               monthlyStars == trend.monthlyStars;
    }

    @Override
    public int hashCode() {
        return Objects.hash(repositoryName, linkToRepository, totalStars, monthlyStars, description, trendsDate);
    }
}
