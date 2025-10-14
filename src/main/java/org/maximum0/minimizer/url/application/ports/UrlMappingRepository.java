package org.maximum0.minimizer.url.application.ports;

import java.util.Optional;
import org.maximum0.minimizer.url.domain.ShortKey;
import org.maximum0.minimizer.url.domain.UrlMapping;

public interface UrlMappingRepository {
    /**
     * UrlMapping을 생성 및 변경합니다.
     * @param mapping 생성 및 변경할 UrlMapping
     * @return 생성 및 변경된 UrlMapping 객체
     */
    UrlMapping save(UrlMapping mapping);

    /**
     * ShortKey로 UrlMapping을 조회합니다.
     * @param shortKey 조회할 단축 URL 키
     * @return 조회한 UrlMapping 객체
     */
    Optional<UrlMapping> findByShortKey(ShortKey shortKey);

}
