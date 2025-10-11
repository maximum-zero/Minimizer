package org.maximum0.minimizer.common.common;

import lombok.NoArgsConstructor;

/**
 *  Base62 인코딩
 *  - 숫자를 Base62 문자열로 변환하고 6자리로 패딩합니다.
 */
@NoArgsConstructor
public class Base62Encoder {
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = 62;
    private static final int KEY_LENGTH = 6;

    /**
     * 고유한 ID를 Base62 문자열로 인코딩합니다.
     * - 동일한 ID를 사용하면, 동일한 인코딩 결과값이 나올 수 있습니다.
     * @param value 인코딩할 고유한 ID 값
     * @return Base62 문자열
     */
    public static String encode(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("encoding 값은 음수일 수 없습니다.");
        }

        if (value == 0) {
            return "000000";
        }

        StringBuilder sb = new StringBuilder();

        long currentValue = value;
        while (currentValue > 0) {
            sb.append(BASE62.charAt((int) (currentValue % BASE)));
            currentValue /= BASE;
        }

        StringBuilder base62Sb = new StringBuilder(sb.reverse().toString());
        while(base62Sb.length() < KEY_LENGTH) {
            base62Sb.insert(0, "0");
        }

        return base62Sb.toString();
    }

}
