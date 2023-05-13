package com.Tiguarces.ProgrammingLanguages.model.language;

import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeIndex;
import com.Tiguarces.ProgrammingLanguages.model.trend.LanguageTrend;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.LanguageParser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
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
    private Integer id;

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
    private int firstAppeared;

    @OneToMany(orphanRemoval = true, cascade = ALL)
    @JoinColumn(name = "TiobeId", referencedColumnName = "LanguageId")
    private List<TiobeIndex> tiobeIndexList;

    @OneToMany(orphanRemoval = true, cascade = ALL)
    @JoinColumn(name = "TrendId", referencedColumnName = "LanguageId")
    private List<LanguageTrend> languageTrends;

    public static Language toEntity(final LanguageParser.DoneLanguage doneLanguage) {
        return new Language(null,
                doneLanguage.getName(),
                doneLanguage.getParadigms(),
                doneLanguage.getFileExtensions(),
                doneLanguage.getStableRelease(),
                doneLanguage.getDescription(),
                doneLanguage.getWebsite(),
                doneLanguage.getImplementations(),
                doneLanguage.getThumbnailPath(),
                doneLanguage.getFirstAppeared(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }
}
