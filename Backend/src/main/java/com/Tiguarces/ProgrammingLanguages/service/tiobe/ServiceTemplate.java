package com.Tiguarces.ProgrammingLanguages.service.tiobe;

import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeIndex;

import java.util.List;

interface ServiceTemplate {
    void saveAll(List<TiobeIndex> doneIndexes);
}
