package org.maximum0.minimizer.url.application;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.maximum0.minimizer.url.application.ports.UrlMappingRepository;
import org.maximum0.minimizer.url.domain.ShortKey;
import org.maximum0.minimizer.url.domain.UrlMapping;
import org.maximum0.minimizer.url.domain.exception.UrlAccessExpiredException;
import org.maximum0.minimizer.url.domain.exception.UrlNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedirectService {
    private final UrlMappingRepository urlMappingRepository;
    private final Clock clock;

    /**
     * ShortKey에 해당하는 원본 URL를 조회합니다.
     * - 조회 시 클릭 회수를 증가시킵니다.
     * @param shortKeyStr 단축 URL 키 문자열
     * @return 매핑된 원본 URL
     * @throws IllegalArgumentException 단축 URL을 찾을 수 없습니다.
     * @throws IllegalStateException 단축 URL이 만료되었습니다.
     */
    public String getOriginalUrl(String shortKeyStr) {
        ShortKey shortKey = ShortKey.createShortKey(shortKeyStr);
        Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findByShortKey(shortKey);
        UrlMapping urlMapping = optionalUrlMapping.orElseThrow(() -> new UrlNotFoundException(shortKey));
        if (urlMapping.isExpired(Instant.now(clock))) {
            throw new UrlAccessExpiredException(shortKey);
        }

        urlMapping.increaseClickCount();
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return savedUrlMapping.getOriginalUrl();
    }
}
