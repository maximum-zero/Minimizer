package org.maximum0.minimizer.url.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maximum0.minimizer.url.application.ports.UrlMappingRepository;
import org.maximum0.minimizer.url.domain.ShortKey;
import org.maximum0.minimizer.url.domain.UrlMapping;
import org.maximum0.minimizer.url.domain.exception.UrlAccessExpiredException;
import org.maximum0.minimizer.url.domain.exception.UrlNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Url Redirect 서비스 로직")
class RedirectServiceTest {
    @Mock
    private Clock clock;
    @Mock
    private UrlMappingRepository urlMappingRepository;

    @InjectMocks
    private RedirectService redirectService;

    private final String ORIGINAL_URL = "https://www.google.com";
    private final String VALID_SHORT_KEY = "000001";

    private Instant fixedTestTime;

    @BeforeEach
    void setUp() {
        fixedTestTime = Instant.now();
    }

    @DisplayName("정상 URL 조회 시, 정상적으로 URL을 반환하고, 클릭수가 증가합니다.")
    @Test
    void givenCreated_whenGetOriginUrl_thenReturnOriginalUrlAndIncreaseClickCount() {
        // given
        UrlMapping mapping = UrlMapping.createUrlMapping(
                1L, VALID_SHORT_KEY, ORIGINAL_URL, fixedTestTime.plus(1, ChronoUnit.DAYS), 0L
        );
        when(clock.instant()).thenReturn(fixedTestTime);
        when(urlMappingRepository.findByShortKey(any(ShortKey.class)))
                .thenReturn(Optional.of(mapping));
        when(urlMappingRepository.save(any(UrlMapping.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        String resultOriginalUrl = redirectService.getOriginalUrl(VALID_SHORT_KEY);

        // then
        assertEquals(ORIGINAL_URL, resultOriginalUrl);
        assertEquals(1L, mapping.getClickCount());

        // when
        String resultOriginalUrl2 = redirectService.getOriginalUrl(VALID_SHORT_KEY);

        // then
        assertEquals(ORIGINAL_URL, resultOriginalUrl2);
        assertEquals(2L, mapping.getClickCount());
    }

    @DisplayName("존재하지 않는 URL 조회 시, UrlNotFoundException 예외가 발생합니다.")
    @Test
    void givenInvalidUrl_whenGetOriginalUrl_thenThrowUrlNotFoundException() {
        // given
        final String invalidUrl = "URL001";
        when(urlMappingRepository.findByShortKey(any(ShortKey.class)))
                .thenReturn(Optional.empty());


        // when & then
        assertThrows(UrlNotFoundException.class, () -> redirectService.getOriginalUrl(invalidUrl));
        verify(urlMappingRepository, never()).save(any());
    }

    @DisplayName("만료된 URL 조회 시, UrlAccessExpiredException 예외가 발생합니다.")
    @Test
    void givenExpired_whenGetOriginalUrl_thenThrowUrlAccessExpiredException() {
        // given
        final String EXPIRED_SHORT_KEY = "EXPIRE";
        UrlMapping mapping = UrlMapping.createUrlMapping(
                9999L, EXPIRED_SHORT_KEY, ORIGINAL_URL, fixedTestTime.minus(1, ChronoUnit.HOURS), 0L
        );
        when(clock.instant()).thenReturn(fixedTestTime);
        when(urlMappingRepository.findByShortKey(any(ShortKey.class)))
                .thenReturn(Optional.of(mapping));

        // when & then
        assertThrows(UrlAccessExpiredException.class, () -> redirectService.getOriginalUrl(EXPIRED_SHORT_KEY));
        verify(urlMappingRepository, never()).save(any());
        assertEquals(0L, mapping.getClickCount());
    }

}