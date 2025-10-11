package org.maximum0.minimizer.url.domain;

import java.time.Instant;
import lombok.Getter;
import org.maximum0.minimizer.common.domain.PositiveCounter;

public class UrlMapping {
    @Getter
    private final Long id;
    private ShortKey shortKey;
    private Url originalUrl;
    private PositiveCounter clickCount;
    private final Instant expiresAt;

    public UrlMapping(Long id, String shortKey, String originalUrl, Instant expiresAt) {
        this.id = id;
        this.shortKey = new ShortKey(shortKey);
        this.originalUrl = new Url(originalUrl);
        this.clickCount = new PositiveCounter();
        this.expiresAt = expiresAt;
    }

    public UrlMapping(Long id, String shortKey, String originalUrl, Instant expiresAt, Long clickCount) {
        this.id = id;
        this.shortKey = new ShortKey(shortKey);
        this.originalUrl = new Url(originalUrl);
        this.expiresAt = expiresAt;
        this.clickCount = new PositiveCounter(clickCount);
    }

    /**
     *  URL 접근 시 호출되며, 접근 횟수를 증가시킵니다.
     */
    public void increaseClickCount() {
        this.clickCount.increase();
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
