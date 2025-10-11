package org.maximum0.minimizer.url.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UrlMapping 도메인")
class UrlMappingTest {
    private final Long ID = 1L;
    private final String SHORT_KEY = "abc12D";
    private final String ORIGINAL_URL = "https://www.google.com";
    private final Instant EXPIRES_AT = Instant.now().plusSeconds(3600);

    // --- 1. 성공적인 생성 테스트 ---

    @DisplayName("클릭 횟수를 제외한 유효한 URL 매핑 생성 시, 클릭 횟수는 0으로 초기화된다.")
    @Test
    void givenValidMappingUrl_whenCreatedWithNoClickCount_thenReturnClickCountIsZero() {
        // when
        UrlMapping mapping = new UrlMapping(ID, SHORT_KEY, ORIGINAL_URL, EXPIRES_AT);

        // then
        assertNotNull(mapping);
        assertEquals(ID, mapping.getId());
        assertEquals(SHORT_KEY, mapping.getShortKey());
        assertEquals(ORIGINAL_URL, mapping.getOriginalUrl());
        assertEquals(0L, mapping.getClickCount());
    }

    @DisplayName("클릭 횟수를 포함한 유효한 URL 매핑 생성 시, 클릭 횟수는 입력한 값으로 초기화된다.")
    @Test
    void givenValidDataForExistingMapping_whenConstructorIsCalled_thenClickCountMatchesInput() {
        // given
        Long expectedClickCount = 42L;

        // when
        UrlMapping mapping = new UrlMapping(ID, SHORT_KEY, ORIGINAL_URL, EXPIRES_AT, expectedClickCount);

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
        assertThrows(IllegalArgumentException.class, () -> new UrlMapping(ID, invalidShortKey, ORIGINAL_URL, EXPIRES_AT));
    }

    @DisplayName("유효하지 않은 OriginalUrl 값으로 생성 시, IllegalArgumentException 예외가 발생합니다.")
    @Test
    void givenInvalidOriginalUrl_whenCreated_thenThrowIllegalArgumentException() {
        // given
        String invalidUrl = "ftp://www.naver.com"; // Url 규칙 위반

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new UrlMapping(ID, SHORT_KEY, invalidUrl, EXPIRES_AT));
    }

    @DisplayName("Url 매핑 생성 후, increaseClickCount() 호출 시, 클릭 횟수가 1 증가합니다.")
    @Test
    void givenUrlMapping_whenIncreaseClickCountIsCalled_thenCountIncreases() {
        // given
        UrlMapping mapping = new UrlMapping(ID, SHORT_KEY, ORIGINAL_URL, EXPIRES_AT);

        // when
        mapping.increaseClickCount();
        mapping.increaseClickCount();

        // then
        assertEquals(2, mapping.getClickCount());
    }

}