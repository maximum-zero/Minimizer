package org.maximum0.minimizer.url.infra.adapters;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.maximum0.minimizer.url.application.ports.SequenceRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class SequenceRepositoryAdapter implements SequenceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final String SEQUENCE_NAME = "url_sequence";

    @Transactional
    @Override
    public long getNextId() {
        Long nextId = ((Number) entityManager.createNativeQuery("SELECT nextval('" + SEQUENCE_NAME + "')")
                .getSingleResult())
                .longValue();

        if (nextId == null) {
            throw new IllegalStateException("DB로부터 다음 시퀀스 ID를 가져오는 데 실패했습니다.");
        }
        return nextId;
    }

}
