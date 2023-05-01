package com.Tiguarces.ProgrammingLanguages.model.language;

import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeIndex;
import com.Tiguarces.ProgrammingLanguages.model.trend.LanguageTrend;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Language")
public class Language {

    @Id
    @Column(name = "LanguageId")
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column(name = "Name", nullable = false, unique = true)
    private String name;

    @Column(name = "Paradigms")
    private String paradigms;

    @Column(name = "FileExtensions")
    private String fileExtensions;

    @Column(name = "StableRelease")
    private String stableRelease;

    @Column(name = "Description", nullable = false)
    private String description;

    @Column(name = "Website")
    private String website;

    @Column(name = "Implementations")
    private String implementations;

    @Column(name = "ThumbnailPath", nullable = false)
    private String thumbnailPath;

    @Column(name = "FirstAppeared", nullable = false)
    private LocalDate firstAppeared;

    @OneToMany(orphanRemoval = true, cascade = ALL)
    @JoinColumn(name = "TiobeIndexId", referencedColumnName = "LanguageId")
    private List<TiobeIndex> tiobeIndexList;

    @OneToMany(orphanRemoval = true, cascade = ALL)
    @JoinColumn(name = "TrendId", referencedColumnName = "LanguageId")
    private List<LanguageTrend> gitHubTrendList;
}
