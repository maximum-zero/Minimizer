package org.maximum0.minimizer.url.domain.exception;

import org.maximum0.minimizer.url.domain.ShortKey;

/**
 * Shorten Url Key에 해당하는 원본 URL을 찾지 못했을 때 발생하는 예외입니다.
 */
public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String shortKey) {
        super(shortKey + "에 해당하는 원본 URL을 찾을 수 없습니다.");
    }

    public UrlNotFoundException(ShortKey shortKey) {
        this(shortKey.getValue());
    }

}
