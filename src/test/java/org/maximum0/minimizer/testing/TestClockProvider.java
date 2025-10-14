package org.maximum0.minimizer.testing;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class TestClockProvider {
    private Clock clock = Clock.systemDefaultZone();

    public Clock getClock() {
        return this.clock;
    }

    public void setFixedTime(Instant fixedTime) {
        this.clock = Clock.fixed(fixedTime, ZoneId.systemDefault());
    }

    public void reset() {
        this.clock = Clock.systemDefaultZone();
    }
}
