package org.maximum0.minimizer.url.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maximum0.minimizer.url.application.ports.SequenceRepository;
import org.maximum0.minimizer.url.application.ports.UrlMappingRepository;
import org.maximum0.minimizer.url.domain.UrlMapping;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Url 서비스 로직")
class UrlServiceTest {
    @Mock
    private Clock clock;
    @Mock
    private SequenceRepository sequenceRepository;
    @Mock
    private UrlMappingRepository urlMappingRepository;

    @InjectMocks
    private UrlService urlService;

    private long currentId;
    private Instant fixedTestTime;

    private final String ORIGINAL_URL = "https://www.google.com";

    @BeforeEach
    void setUp() {
        currentId = 0;
        fixedTestTime = Instant.now();
        when(clock.instant()).thenReturn(fixedTestTime);
        when(urlMappingRepository.save(any(UrlMapping.class)))
                .thenAnswer(this::injectIdAndReturn);

        DbSequenceShortKeyGenerator sequenceGenerator = new DbSequenceShortKeyGenerator(sequenceRepository);
        urlService = new UrlService(sequenceGenerator, urlMappingRepository, clock);
    }

    @DisplayName("유효한 URL로 Shorten URL 생성 후 반환된 객체가 정확한지 확인합니다.")
    @Test
    void givenValidUrl_whenCreatedShortenUrl_thenReturnUrlMapping() {
        // given
        when(sequenceRepository.getNextId()).thenReturn(1L);

        // when
        UrlMapping result = urlService.createShortenUrl(ORIGINAL_URL);

        // then
        assertNotNull(result.getId());
        assertEquals("000001", result.getShortKey());
        assertEquals(ORIGINAL_URL, result.getOriginalUrl());
        Instant expectedExpireAt = fixedTestTime.plus(10, ChronoUnit.DAYS);
        assertEquals(expectedExpireAt, result.getExpiresAt());
    }

    private UrlMapping injectIdAndReturn(InvocationOnMock invocation) {
        UrlMapping savedMapping = invocation.getArgument(0);
        return UrlMapping.createUrlMapping(
                currentId,
                savedMapping.getShortKey(),
                savedMapping.getOriginalUrl(),
                savedMapping.getExpiresAt(),
                savedMapping.getClickCount()
        );
    }
}
