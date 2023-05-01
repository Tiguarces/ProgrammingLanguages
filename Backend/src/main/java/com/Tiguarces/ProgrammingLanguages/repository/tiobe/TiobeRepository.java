package com.Tiguarces.ProgrammingLanguages.repository.tiobe;

import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiobeRepository extends JpaRepository<TiobeIndex, Integer> {
}
