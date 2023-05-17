package com.Tiguarces.ProgrammingLanguages.service.tiobe;

import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeIndex;
import com.Tiguarces.ProgrammingLanguages.repository.tiobe.TiobeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TiobeService implements ServiceTemplate {
    private final TiobeRepository tiobeRepository;

    @Override
    public void saveAll(final List<TiobeIndex> doneIndexes) {
        tiobeRepository.saveAll(doneIndexes);
    }
}
