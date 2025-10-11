package org.maximum0.minimizer.common.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PositiveCounter {
    private long value;

    public PositiveCounter() {
        this.value = 0L;
    }

    public PositiveCounter(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("카운트 값은 음수일 수 없습니다.");
        }
        this.value = value;
    }

    /**
     *  값을 증가시킵니다.
     */
    public void increase() {
        this.value++;
    }

    /**
     *  값을 감소시킵니다.
     */
    public void decrease() {
        if (value <= 0) {
            return;
        }
        this.value--;
    }

}
