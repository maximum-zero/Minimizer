package org.maximum0.minimizer.url.application;

import java.time.Clock;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.maximum0.minimizer.url.application.interfaces.ShortKeyGenerator;
import org.maximum0.minimizer.url.application.ports.UrlMappingRepository;
import org.maximum0.minimizer.url.domain.ShortKey;
import org.maximum0.minimizer.url.domain.UrlMapping;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final ShortKeyGenerator shortKeyGenerator;
    private final UrlMappingRepository urlMappingRepository;
    private final Clock clock;

    private final static long EXPIRE_AT = 60 * 60 * 24 * 10; // 10d

    /**
     * 새로운 단축 URL을 생성합니다.
     * @param url 원본 URL
     * @return 생성된 단축 URL의 UrlMapping 객체
     */
    @Transactional
    public UrlMapping createShortenUrl(String url) {
        ShortKey shortKey = shortKeyGenerator.generate();
        Instant expireAt = Instant.now(clock).plusSeconds(EXPIRE_AT);
        UrlMapping urlMapping = UrlMapping.createUrlMapping(shortKey, url, expireAt);
        return urlMappingRepository.save(urlMapping);
    }

}
