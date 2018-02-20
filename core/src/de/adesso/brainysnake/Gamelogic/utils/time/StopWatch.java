package de.adesso.brainysnake.Gamelogic.utils.time;

import java.util.concurrent.TimeUnit;

/**
 * A simple stopwatch
 */
public class StopWatch {

    private long statingTime;
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    public static StopWatch start() {
        return new StopWatch();
    }

    private StopWatch() {
        this.reset();
    }

    public void reset() {
        this.statingTime = System.currentTimeMillis();
    }

    public long time() {
        return System.currentTimeMillis() - this.statingTime;
    }

    public long time(TimeUnit unit) {
        return unit.convert(time(), this.timeUnit);
    }

    public long getStatingTime() {
        return statingTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
