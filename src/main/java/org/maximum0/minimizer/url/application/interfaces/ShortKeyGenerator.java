package org.maximum0.minimizer.url.application.interfaces;

import org.maximum0.minimizer.url.domain.ShortKey;

public interface ShortKeyGenerator {

    /**
     * ShortKey를 생성하여 반환합니다.
     * @return ShortKey 도메인 객체
     */
    ShortKey generate();
}
