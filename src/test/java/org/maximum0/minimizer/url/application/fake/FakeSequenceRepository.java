package org.maximum0.minimizer.url.application.fake;

import org.maximum0.minimizer.url.application.ports.SequenceRepository;

public class FakeSequenceRepository implements SequenceRepository {
    private long currentId;

    public FakeSequenceRepository(long currentId) {
        this.currentId = currentId;
    }

    @Override
    public long getNextId() {
        return ++currentId;
    }
}
