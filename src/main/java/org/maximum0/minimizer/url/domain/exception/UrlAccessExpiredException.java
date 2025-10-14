package org.maximum0.minimizer.url.domain.exception;

import org.maximum0.minimizer.url.domain.ShortKey;

/**
 * Shorten Url이 만료되었을 때 발생하는 예외입니다.
 */
public class UrlAccessExpiredException extends RuntimeException {
    public UrlAccessExpiredException(String shortKey) {
        super(shortKey + "는 만료되었습니다");
    }

    public UrlAccessExpiredException(ShortKey shortKey) {
        this(shortKey.getValue());
    }

}
