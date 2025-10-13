package org.maximum0.minimizer.url.application;

import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.maximum0.minimizer.url.application.interfaces.ShortKeyGenerator;
import org.maximum0.minimizer.url.application.ports.UrlMappingRepository;
import org.maximum0.minimizer.url.domain.ShortKey;
import org.maximum0.minimizer.url.domain.UrlMapping;
import org.maximum0.minimizer.url.domain.exception.UrlNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final ShortKeyGenerator shortKeyGenerator;
    private final UrlMappingRepository urlMappingRepository;

    /**
     * 단축 URL의 기본 만료 기간
     */
    private final static long EXPIRE_AT = 60 * 60 * 24 * 10; // 10d

    /**
     * 새로운 단축 URL을 생성합니다.
     * @param url 원본 URL
     * @return 생성된 단축 URL의 UrlMapping 객체
     */
    public UrlMapping createShortenUrl(String url) {
        ShortKey shortKey = shortKeyGenerator.generate();
        Instant expireAt = Instant.now().plusSeconds(EXPIRE_AT);
        UrlMapping urlMapping = UrlMapping.createUrlMapping(shortKey, url, expireAt);
        return urlMappingRepository.save(urlMapping);
    }

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
        if (urlMapping.isExpired()) {
            throw new IllegalStateException("단축 URL이 만료되었습니다.");
        }

        urlMapping.increaseClickCount();
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return savedUrlMapping.getOriginalUrl();
    }

}
