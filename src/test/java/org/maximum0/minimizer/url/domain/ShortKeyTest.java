package org.maximum0.minimizer.url.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ShortKey 도메인")
class ShortKeyTest {
    private static final int KEY_LENGTH = 6;

    @DisplayName("유효한 값으로 생성 시, 6자리의 키를 반환합니다.")
    @ParameterizedTest(name = "유효한 키: {0}")
    @ValueSource(strings = {"abc12D", "AbCdEF", "123456"})
    void givenValidKey_whenCreated_thenIsCreatedAndReturnSixLengthValue(String validKey) {
        // when
        ShortKey shortKey = ShortKey.createShortKey(validKey);

        // then
        assertEquals(KEY_LENGTH, shortKey.getValue().length());
        assertEquals(validKey, shortKey.getValue());
    }

    @DisplayName("빈 값으로 생성 시, IllegalArgumentException 예외가 발생합니다.")
    @ParameterizedTest(name = "입력 값: {0}")
    @NullAndEmptySource
    void givenEmptyKey_whenCreated_thenThrowIllegalArgumentException(String invalidKey) {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> ShortKey.createShortKey(invalidKey));
    }

    @DisplayName("6자가 아니거나 유효하지 않은 문자를 포함 시, IllegalArgumentException 예외가 발생합니다.")
    @ParameterizedTest(name = "입력 값: {0}")
    @ValueSource(strings = {
            "abc",      // 6자 미만
            "1234567",  // 7자 초과
            "abc12-",   // 유효하지 않은 문자(-) 포함
            "123!@#"    // 유효하지 않은 문자 포함
    })
    void givenInvalidLengthOrCharacters_whenCreated_thenThrowIllegalArgumentException(String invalidKey) {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> ShortKey.createShortKey(invalidKey));
    }

}