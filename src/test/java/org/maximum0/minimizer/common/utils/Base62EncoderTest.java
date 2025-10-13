package org.maximum0.minimizer.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Base62Encoder 유틸리티")
class Base62EncoderTest {

    @DisplayName("유효한 값으로 인코딩 시, 6자리의 Base62 문자열을 반환합니다.")
    @ParameterizedTest(name = "ID {0}을 인코딩하면 {1}이 되며, 길이는 6자여야 합니다.")
    @CsvSource({
        "0, 000000",
        "1, 000001",
        "61, 00000z",
        "62, 000010",
        "1000, 0000G8",
        "5000000, 00KyjA",
        "56800235583, zzzzzz"
    })
    void givenLongId_whenEncoded_thenReturnSixCharacters(long input, String expected) {
        // when
        String result = Base62Encoder.encode(input);

        // then
        assertEquals(6, result.length());
        assertEquals(expected, result);
    }

    @DisplayName("음수 값으로 인코딩 시, IllegalArgumentException 예외가 발생합니다.")
    @Test
    void givenNegativeId_whenEncoded_thenThrowIllegalArgumentException() {
        // given
        long negativeId = -1L;

        // when & then
        assertThrows(IllegalArgumentException.class, () -> Base62Encoder.encode(negativeId));
    }

}
