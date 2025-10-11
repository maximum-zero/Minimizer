package org.maximum0.minimizer.url.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Url 도메인")
class UrlTest {
    @DisplayName("유효한 URL 값으로 생성 시, 정상적으로 생성되고 보유한 값을 반환합니다.")
    @ParameterizedTest(name = "유효한 URL: {0}")
    @ValueSource(strings = {
        "https://www.google.com/search?q=minimizer",
        "https://www.naver.com",
        "https://example.com"
    })
    void givenValidValue_whenCreated_thenIsCreatedAndReturnValue(String validUrl) {
        // when
        Url url = Url.createUrl(validUrl);

        // then
        assertNotNull(url);
        assertEquals(validUrl, url.getValue());
    }

    @DisplayName("빈 값으로 생성 시, IllegalArgumentException 예외가 발생합니다.")
    @ParameterizedTest(name = "입력 값: {0}")
    @NullAndEmptySource
    void givenEmptyValue_whenCreated_thenThrowIllegalArgumentException(String emptyValue) {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> Url.createUrl(emptyValue));
    }

    @DisplayName("유효하지 않은 URL 형식으로 생성 시, IllegalArgumentException 예외가 발생합니다.")
    @ParameterizedTest(name = "유효하지 않은 URL: {0}")
    @ValueSource(strings = {
        "invalidUrl", // 스키마 없음
        "ftp://www.invalid.com", // 지원하지 않는 프로토콜
        "http://.com", // URL 형식 오류
    })
    void givenInvalidValue_whenCreated_thenThrowIllegalArgumentException(String invalidValue) {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> Url.createUrl(invalidValue));
    }
}