package org.maximum0.minimizer.url.infra.jpa;

import java.util.Optional;
import org.maximum0.minimizer.url.infra.entities.UrlMappingEntity;

public interface UrlMappingJpaRepository {
    UrlMappingEntity save(UrlMappingEntity urlMapping);
    Optional<UrlMappingEntity> findByShortKey(String shortKey);

}
