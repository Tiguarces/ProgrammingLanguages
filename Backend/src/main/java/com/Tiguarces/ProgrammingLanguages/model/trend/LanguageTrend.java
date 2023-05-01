package com.Tiguarces.ProgrammingLanguages.model.trend;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private int id;

    @Column(name = "RepositoryName", nullable = false, unique = true)
    private String repositoryName;

    @Column(name = "LinkToRepository", nullable = false, unique = true)
    private String linkToRepository;

    @Column(name = "TotalStars", nullable = false)
    private int totalStars;

    @Column(name = "MonthlyStars", nullable = false)
    private int monthlyStars;

    @Column(name = "TrendsDate", nullable = false)
    private LocalDate trendsDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LanguageId", referencedColumnName = "LanguageId")
    private Language language;
}
