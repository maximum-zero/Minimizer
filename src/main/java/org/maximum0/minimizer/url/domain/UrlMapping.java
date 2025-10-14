package org.maximum0.minimizer.url.domain;

import java.time.Instant;
import lombok.Getter;
import org.maximum0.minimizer.common.domain.PositiveCounter;

public class UrlMapping {
    @Getter
    private final Long id;
    private final ShortKey shortKey;
    private final Url originalUrl;
    private final PositiveCounter clickCount;
    @Getter
    private final Instant expiresAt;

    public static UrlMapping createUrlMapping(ShortKey shortKey, String originalUrl, Instant expiresAt) {
        return new UrlMapping(null, shortKey, originalUrl, expiresAt, 0L);
    }

    public static UrlMapping createUrlMapping(Long id, String shortKey, String originalUrl, Instant expiresAt, Long clickCount) {
        return new UrlMapping(id, ShortKey.createShortKey(shortKey), originalUrl, expiresAt, clickCount);
    }

    private UrlMapping(Long id, ShortKey shortKey, String originalUrl, Instant expiresAt, Long clickCount) {
        this.id = id;
        this.shortKey = shortKey;
        this.originalUrl = Url.createUrl(originalUrl);
        this.expiresAt = expiresAt;

        if (clickCount == null) {
            clickCount = 0L;
        }
        this.clickCount = new PositiveCounter(clickCount);
    }

    /**
     *  URL 접근 시 호출되며, 접근 횟수를 증가시킵니다.
     */
    public void increaseClickCount() {
        this.clickCount.increase();
    }

    /**
     * 단축 URL이 만료되었는지 확인합니다.
     * @return 만료시 true, 아니면 false
     */
    public boolean isExpired(Instant now) {
        if (this.expiresAt == null) {
            return false;
        }
        return this.expiresAt.isBefore(now);
    }

    public String getShortKey() {
        return this.shortKey.getValue();
    }

    public String getOriginalUrl() {
        return this.originalUrl.getValue();
    }

    public Long getClickCount() {
        return this.clickCount.getValue();
    }
}
