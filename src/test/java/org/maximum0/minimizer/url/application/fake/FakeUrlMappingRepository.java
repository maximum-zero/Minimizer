package org.maximum0.minimizer.url.application.fake;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.maximum0.minimizer.url.application.ports.UrlMappingRepository;
import org.maximum0.minimizer.url.domain.ShortKey;
import org.maximum0.minimizer.url.domain.UrlMapping;

public class FakeUrlMappingRepository implements UrlMappingRepository {
    private final Map<String, UrlMapping> store = new HashMap<>();
    private final AtomicLong currentId = new AtomicLong(1);

    @Override
    public UrlMapping save(UrlMapping mapping) {
        Long id = mapping.getId() != null ? mapping.getId() : currentId.getAndIncrement();
        UrlMapping savedMapping = UrlMapping.createUrlMapping(
                id,
                mapping.getShortKey(),
                mapping.getOriginalUrl(),
                mapping.getExpiresAt(),
                mapping.getClickCount()
        );
        store.put(savedMapping.getShortKey(), savedMapping);
        return savedMapping;
    }

    @Override
    public Optional<UrlMapping> findByShortKey(ShortKey shortKey) {
        return Optional.ofNullable(store.get(shortKey.getValue()));
    }

}
