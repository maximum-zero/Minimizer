package org.maximum0.minimizer.testing;

import org.maximum0.minimizer.url.application.DbSequenceShortKeyGenerator;
import org.maximum0.minimizer.url.application.UrlService;
import org.maximum0.minimizer.url.application.fake.FakeSequenceRepository;
import org.maximum0.minimizer.url.application.fake.FakeUrlMappingRepository;
import org.maximum0.minimizer.url.application.ports.SequenceRepository;
import org.maximum0.minimizer.url.application.ports.UrlMappingRepository;

public class FakeObjectFactory {
    private static final SequenceRepository sequenceRepository = new FakeSequenceRepository(0);
    private static final UrlMappingRepository urlMappingRepository = new FakeUrlMappingRepository();

    private static final DbSequenceShortKeyGenerator dbSequenceShortKeyGenerator = new DbSequenceShortKeyGenerator(sequenceRepository);
    private static final UrlService urlService = new UrlService(dbSequenceShortKeyGenerator, urlMappingRepository);

    private FakeObjectFactory() {}

    public static DbSequenceShortKeyGenerator createDbSequenceShortKeyGenerator(long initialId) {
        return new DbSequenceShortKeyGenerator(new FakeSequenceRepository(initialId));
    }

    public static DbSequenceShortKeyGenerator getDbSequenceShortKeyGenerator() {
        return dbSequenceShortKeyGenerator;
    }

    public static UrlService getUrlService() {
        return urlService;
    }

    public static UrlMappingRepository getUrlMappingRepository() {
        return urlMappingRepository;
    }

}
