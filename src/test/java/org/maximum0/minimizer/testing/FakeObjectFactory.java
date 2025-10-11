package org.maximum0.minimizer.testing;

import org.maximum0.minimizer.url.application.DbSequenceShortKeyGenerator;
import org.maximum0.minimizer.url.application.fake.FakeSequenceRepository;
import org.maximum0.minimizer.url.application.ports.SequenceRepository;

public class FakeObjectFactory {
    private static final SequenceRepository sequenceRepository = new FakeSequenceRepository(1);

    private static final DbSequenceShortKeyGenerator dbSequenceShortKeyGenerator = new DbSequenceShortKeyGenerator(sequenceRepository);

    private FakeObjectFactory() {}

    public static DbSequenceShortKeyGenerator createDbSequenceShortKeyGenerator(long initialId) {
        return new DbSequenceShortKeyGenerator(new FakeSequenceRepository(initialId));
    }

    public static DbSequenceShortKeyGenerator getDbSequenceShortKeyGenerator() {
        return dbSequenceShortKeyGenerator;
    }

}
