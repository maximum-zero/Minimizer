package org.maximum0.minimizer.url.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ShortKey {
    /**
     *  KEY 길이별 Base62 기준 키 개수
     *  5 - 916,132,832개 (62^5)
     *  6 - 56,800,235,584개 (62^6)
     *  7 - 3,521,614,606,208개 (62^7)
     */
    private static final int KEY_LENGTH = 6;

    private final String value;

    public static ShortKey createShortKey(String value) {
        return new ShortKey(value);
    }

    private ShortKey(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("단축 URL 키는 비어있을 수 없습니다.");
        }

        if (value.length() != KEY_LENGTH) {
            throw new IllegalArgumentException("단축 URL 키는 " + KEY_LENGTH + "자여야 합니다.");
        }

        if (!value.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("단축 URL 키는 영문 대소문자와 숫자만 포함해야 합니다.");
        }

        this.value = value;
    }

}
