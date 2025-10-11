package org.maximum0.minimizer.url.infra.adapters;

import lombok.RequiredArgsConstructor;
import org.maximum0.minimizer.url.application.ports.SequenceRepository;
import org.maximum0.minimizer.url.infra.jpa.SequenceJpaRepository;

@RequiredArgsConstructor
public class SequenceRepositoryAdapter implements SequenceRepository {
    private final SequenceJpaRepository sequenceJpaRepository;

    @Override
    public long getNextId() {
        Long nextId = sequenceJpaRepository.getNextId();
        if (nextId == null) {
            throw new IllegalStateException("DB로부터 다음 시퀀스 ID를 가져오는 데 실패했습니다.");
        }

        return nextId;
    }

}
