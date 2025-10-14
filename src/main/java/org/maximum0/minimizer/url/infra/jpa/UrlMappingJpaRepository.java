package org.maximum0.minimizer.url.infra.jpa;

import java.util.Optional;
import org.maximum0.minimizer.url.infra.entities.UrlMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMappingJpaRepository extends JpaRepository<UrlMappingEntity, Long> {
    Optional<UrlMappingEntity> findByShortKey(String shortKey);

}
