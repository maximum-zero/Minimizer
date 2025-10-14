package org.maximum0.minimizer.url.infra.adapters;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.maximum0.minimizer.url.application.ports.UrlMappingRepository;
import org.maximum0.minimizer.url.domain.ShortKey;
import org.maximum0.minimizer.url.domain.UrlMapping;
import org.maximum0.minimizer.url.infra.entities.UrlMappingEntity;
import org.maximum0.minimizer.url.infra.jpa.UrlMappingJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UrlMappingRepositoryAdapter implements UrlMappingRepository {
    private final UrlMappingJpaRepository urlMappingJpaRepository;

    @Override
    public UrlMapping save(UrlMapping mapping) {
        UrlMappingEntity entity = urlMappingJpaRepository.save(toEntity(mapping));
        return toDomain(entity);
    }

    @Override
    public Optional<UrlMapping> findByShortKey(ShortKey shortKey) {
        Optional<UrlMappingEntity> optional = urlMappingJpaRepository.findByShortKey(shortKey.getValue());
        return optional.map(UrlMappingRepositoryAdapter::toDomain);
    }

    private static UrlMappingEntity toEntity(UrlMapping urlMapping) {
        return UrlMappingEntity.builder()
                .id(urlMapping.getId())
                .shortKey(urlMapping.getShortKey())
                .originalUrl(urlMapping.getOriginalUrl())
                .clickCount(urlMapping.getClickCount())
                .expiresAt(urlMapping.getExpiresAt())
                .build();
    }

    private static UrlMapping toDomain(UrlMappingEntity entity) {
        return UrlMapping.createUrlMapping(
                entity.getId(),
                entity.getShortKey(),
                entity.getOriginalUrl(),
                entity.getExpiresAt(),
                entity.getClickCount()
        );
    }

}
