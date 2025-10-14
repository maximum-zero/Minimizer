package org.maximum0.minimizer.url.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UrlMapping 도메인")
class UrlMappingTest {
    private final String SHORT_KEY = "abc12D";
    private final String ORIGINAL_URL = "https://www.google.com";
    private Instant expiresAt;

    @BeforeEach
    void setUp() {
        expiresAt = Instant.now().plusSeconds(3600);
    }


    @DisplayName("클릭 횟수를 제외한 유효한 URL 매핑 생성 시, 클릭 횟수는 0으로 초기화된다.")
    @Test
    void givenValidMappingUrl_whenCreatedWithNoClickCount_thenReturnClickCountIsZero() {
        // given
        ShortKey shortKey = ShortKey.createShortKey(SHORT_KEY);

        // when
        UrlMapping mapping = UrlMapping.createUrlMapping(shortKey, ORIGINAL_URL, expiresAt);

        // then
        assertNotNull(mapping);
        assertEquals(SHORT_KEY, mapping.getShortKey());
        assertEquals(ORIGINAL_URL, mapping.getOriginalUrl());
        assertEquals(0L, mapping.getClickCount());
    }

    @DisplayName("클릭 횟수를 포함한 유효한 URL 매핑 생성 시, 클릭 횟수는 입력한 값으로 초기화된다.")
    @Test
    void givenValidDataForExistingMapping_whenConstructorIsCalled_thenClickCountMatchesInput() {
        // given
        Long ID = 1L;
        Long expectedClickCount = 42L;

        // when
        UrlMapping mapping = UrlMapping.createUrlMapping(ID, SHORT_KEY, ORIGINAL_URL, expiresAt, expectedClickCount);

        // then
        assertNotNull(mapping);
        assertEquals(expectedClickCount, mapping.getClickCount());
    }

    @DisplayName("유효하지 않은 ShortKey 값으로 생성 시, IllegalArgumentException 예외가 발생합니다.")
    @Test
    void givenInvalidShortKey_whenCreated_thenThrowIllegalArgumentException() {
        // given
        String invalidShortKey = "key1"; // 6자 미만

        // when & then
        assertThrows(IllegalArgumentException.class, () -> UrlMapping.createUrlMapping(ShortKey.createShortKey(invalidShortKey), ORIGINAL_URL, expiresAt));
    }

    @DisplayName("유효하지 않은 OriginalUrl 값으로 생성 시, IllegalArgumentException 예외가 발생합니다.")
    @Test
    void givenInvalidOriginalUrl_whenCreated_thenThrowIllegalArgumentException() {
        // given
        ShortKey shortKey = ShortKey.createShortKey(SHORT_KEY);
        String invalidUrl = "ftp://www.naver.com"; // Url 규칙 위반

        // when & then
        assertThrows(IllegalArgumentException.class, () -> UrlMapping.createUrlMapping(shortKey, invalidUrl, expiresAt));
    }

    @DisplayName("Url 매핑 생성 후, increaseClickCount() 호출 시, 클릭 횟수가 1 증가합니다.")
    @Test
    void givenUrlMapping_whenIncreaseClickCountIsCalled_thenCountIncreases() {
        // given
        ShortKey shortKey = ShortKey.createShortKey(SHORT_KEY);
        UrlMapping mapping = UrlMapping.createUrlMapping(shortKey, ORIGINAL_URL, expiresAt);

        // when
        mapping.increaseClickCount();
        mapping.increaseClickCount();

        // then
        assertEquals(2, mapping.getClickCount());
    }

}