package org.maximum0.minimizer.url.application;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.maximum0.minimizer.testing.FakeObjectFactory;
import org.maximum0.minimizer.url.application.ports.UrlMappingRepository;
import org.maximum0.minimizer.url.domain.ShortKey;
import org.maximum0.minimizer.url.domain.UrlMapping;

@DisplayName("Url 서비스 로직")
class UrlServiceTest {
    private final UrlService urlService = FakeObjectFactory.getUrlService();
    private final UrlMappingRepository urlMappingRepository = FakeObjectFactory.getUrlMappingRepository();

    private final String ORIGINAL_URL = "https://www.google.com";

    @DisplayName("유효한 URL로 Shorten URL 생성 후 반환된 객체가 정확한지 확인합니다.")
    @Test
    void givenValidUrl_whenCreatedShortenUrl_thenReturnUrlMapping() {
        // when
        UrlMapping result = urlService.createShortenUrl(ORIGINAL_URL);

        // then
        assertNotNull(result.getId());
        assertEquals("000001", result.getShortKey());
        assertEquals(ORIGINAL_URL, result.getOriginalUrl());
        assertTrue(result.getExpiresAt().isAfter(Instant.now().plus(9, ChronoUnit.DAYS)));
    }

    @DisplayName("정상적으로 Shorten URL를 생성 후, 조회시 정상적으로 URL을 반환하고, 클릭수가 증가합니다.")
    @Test
    void givenCreated_whenGetOriginUrl_thenReturnOriginalUrlAndIncreaseClickCount() {
        // given
        UrlMapping createdMapping = urlService.createShortenUrl(ORIGINAL_URL);
        String shortKeyStr = createdMapping.getShortKey();

        // when
        String resultOriginalUrl = urlService.getOriginalUrl(shortKeyStr);

        // then
        UrlMapping urlMapping = urlMappingRepository.findByShortKey(ShortKey.createShortKey(shortKeyStr)).get();
        assertEquals(ORIGINAL_URL, resultOriginalUrl);
        assertEquals(1L, urlMapping.getClickCount());

        // when
        String resultOriginalUrl2 = urlService.getOriginalUrl(shortKeyStr);

        // then
        UrlMapping urlMapping2 = urlMappingRepository.findByShortKey(ShortKey.createShortKey(shortKeyStr)).get();
        assertEquals(ORIGINAL_URL, resultOriginalUrl2);
        assertEquals(2L, urlMapping2.getClickCount());
    }

    @DisplayName("존재하지 않는 URL 조회 시, IllegalArgumentException 예외가 발생합니다.")
    @Test
    void givenInvalidUrl_whenGetOriginalUrl_thenThrowIllegalArgumentException() {
        // given
        final String invalidUrl = "Invalid Url";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> urlService.getOriginalUrl(invalidUrl));
    }

    @DisplayName("만료된 URL 조회 시, IllegalStateException 예외가 발생합니다.")
    @Test
    void givenExpired_whenGetOriginalUrl_thenThrowIllegalStateException() {
        // given
        final String EXPIRED_SHORT_KEY = "EXPIRE";
        UrlMapping expiredMapping = UrlMapping.createUrlMapping(
                9999L,
                EXPIRED_SHORT_KEY,
                ORIGINAL_URL,
                Instant.now().minus(1, ChronoUnit.HOURS),
                0L
        );
        urlMappingRepository.save(expiredMapping);

        // when & then
        assertThrows(IllegalStateException.class, () -> urlService.getOriginalUrl(EXPIRED_SHORT_KEY));
    }

}
