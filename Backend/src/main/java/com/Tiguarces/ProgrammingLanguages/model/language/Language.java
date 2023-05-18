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
import java.util.Objects;

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

    @OneToMany(orphanRemoval = true, cascade = ALL, mappedBy = "language")
    private List<TiobeIndex> tiobeIndexList;

    @OneToMany(orphanRemoval = true, cascade = ALL, mappedBy = "language")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Language language = (Language) o;
        return Objects.equals(name, language.name)                          &&
               Objects.equals(paradigms, language.paradigms)                &&
               Objects.equals(fileExtensions, language.fileExtensions)      &&
               Objects.equals(stableRelease, language.stableRelease)        &&
               Objects.equals(description, language.description)            &&
               Objects.equals(website, language.website)                    &&
               Objects.equals(implementations, language.implementations)    &&
               Objects.equals(thumbnailPath, language.thumbnailPath)        &&
               firstAppeared == language.firstAppeared;

    }

    @Override
    public int hashCode() {
        return Objects.hash(name, paradigms, fileExtensions, stableRelease, description, website, implementations, thumbnailPath, firstAppeared);
    }
}
