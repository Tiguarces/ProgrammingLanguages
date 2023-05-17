package com.Tiguarces.ProgrammingLanguages.model.tiobe;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;
import com.Tiguarces.ProgrammingLanguages.scraping.parser.TiobeIndexParser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TiobeIndex")
public class TiobeIndex {

    @Id
    @Column(name = "TiobeId")
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(name = "Rank", nullable = false)
    private int rank;

    @Column(name = "IndexDate", nullable = false)
    private LocalDate indexDate;

    @Enumerated(STRING)
    @Column(name = "Status", nullable = false)
    private TiobeStatus status = TiobeStatus.NEUTRAL;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LanguageId")
    private Language language;

    public static TiobeIndex toEntity(final TiobeIndexParser.DoneTiobeIndex tiobeIndexToParse, final LocalDate trendDate, final Language language) {
        return new TiobeIndex(null,
                tiobeIndexToParse.rank(),
                trendDate,
                tiobeIndexToParse.status(),
                language
        );
    }
}
