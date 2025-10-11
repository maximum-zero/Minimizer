package org.maximum0.minimizer.common.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("PositiveCounter 도메인")
class PositiveCounterTest {
    @DisplayName("빈 값으로 생성 시, 값은 0L으로 초기화된다.")
    @Test
    void givenEmpty_whenCreated_thenValueIsZero() {
        // when
        PositiveCounter counter = new PositiveCounter();

        // then
        assertEquals(0L, counter.getValue());
    }

    @DisplayName("유효한 값으로 생성 시, 값은 0L으로 초기화된다.")
    @ParameterizedTest(name = "유효한 값 : {0}")
    @ValueSource(longs = {1L, 100L, 1000L, 999999L})
    void givenValidValue_whenCreated_thenValueIsInitialized(long validValue) {
        // when
        PositiveCounter counter = new PositiveCounter(validValue);

        // then
        assertEquals(validValue, counter.getValue());
    }

    @DisplayName("음수 값으로 생성 시, IllegalArgumentException 예외가 발생합니다.")
    @ParameterizedTest(name = "음수 값: {0}")
    @ValueSource(longs = {-1L, -100L, -999L})
    void givenNegativeValue_whenCreated_thenThrowIllegalArgumentException(long invalidValue) {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> new PositiveCounter(invalidValue));
    }

    @DisplayName("PositiveCounter 생성 후, increase() 호출 시, 값이 1 증가합니다.")
    @Test
    void givenCreatedWithValue_whenIncreaseIsCalled_thenValueIncrementsByOne() {
        // given
        PositiveCounter counter = new PositiveCounter(5L);

        // when
        counter.increase();

        // then
        assertEquals(6L, counter.getValue());
    }

    @DisplayName("0보다 큰 값으로 생성 후, decrease()를 호출하면, 값이 1 감소합니다.")
    @Test
    void givenCreatedWithValueGreaterThanZero_whenDecreaseIsCalled_thenValueDecrementsByOne() {
        // given
        PositiveCounter counter = new PositiveCounter(5L);

        // when
        counter.decrease();

        // then
        assertEquals(4L, counter.getValue());
    }

    @DisplayName("0으로 생성 후, decrease()를 호출하면, 값이 0L로 유지된다.")
    @Test
    void givenCreatedWithValueIsZero_whenDecreaseIsCalled_thenValueZero() {
        // given
        PositiveCounter counter = new PositiveCounter(0L);

        // when
        counter.decrease();
        counter.decrease();

        // then
        assertEquals(0L, counter.getValue());
    }

}
