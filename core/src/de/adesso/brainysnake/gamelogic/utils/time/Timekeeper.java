package de.adesso.brainysnake.gamelogic.utils.time;

import java.util.concurrent.TimeUnit;

/**
 * A simple timer
 */
public class Timekeeper {

    private long timeToKeep;
    private StopWatch stopWatch;

    public static Timekeeper start(long timeToKeep) {
        return new Timekeeper(timeToKeep);
    }

    private Timekeeper(long timeToKeep) {
        this.timeToKeep = timeToKeep;
    }

    public void reset() {
        this.stopWatch = StopWatch.start();
    }

    public long getRemainingTime() {
        return this.timeToKeep - this.stopWatch.time();
    }

    public boolean timeout() {
        return this.getRemainingTime() < 0;
    }

    public TimeUnit getTimeUnit() {
        return this.stopWatch.getTimeUnit();
    }
}
