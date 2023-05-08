package com.Tiguarces.ProgrammingLanguages.service.trends;

import com.Tiguarces.ProgrammingLanguages.model.trend.LanguageTrend;

import java.util.List;

interface ServiceTemplate {
    void saveAll(List<LanguageTrend> trendsToSave);
}
