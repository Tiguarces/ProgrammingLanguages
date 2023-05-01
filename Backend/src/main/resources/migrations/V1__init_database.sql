CREATE TABLE IF NOT EXISTS Language(
    LanguageId INTEGER AUTO_INCREMENT NOT NULL,
    Name NVARCHAR(254) NOT NULL UNIQUE,
    Paradigms NVARCHAR(512),
    FileExtensions NVARCHAR(512),
    StableRelease NVARCHAR(120),
    Description LONGTEXT NOT NULL,
    Website NVARCHAR(256),
    Implementations NVARCHAR(768),
    ThumbnailPath NVARCHAR(1024) NOT NULL,
    FirstAppeared Date NOT NULL,

    PRIMARY KEY (LanguageId)
);

CREATE TABLE IF NOT EXISTS TiobeIndex(
    TiobeId INTEGER AUTO_INCREMENT NOT NULL,
    Rank INTEGER NOT NULL,
    IndexDate DATE NOT NULL,
    Status INTEGER NOT NULL,
    LanguageId INTEGER NOT NULL,

    PRIMARY KEY (TiobeId),
    FOREIGN KEY (LanguageId)
        REFERENCES Language(LanguageId)
);

CREATE TABLE IF NOT EXISTS LanguageTrend(
    TrendId INTEGER AUTO_INCREMENT NOT NULL,
    RepositoryName NVARCHAR(1024) NOT NULL UNIQUE,
    LinkToRepository NVARCHAR(1024) NOT NULL UNIQUE,
    TotalStars INTEGER NOT NULL DEFAULT 0,
    MonthlyStars INTEGER NOT NULL DEFAULT 0,
    TrendsDate Date NOT NULL,
    LanguageId INTEGER NOT NULL,

    PRIMARY KEY (TrendId),
    FOREIGN KEY (LanguageId)
        REFERENCES Language(LanguageId)
);