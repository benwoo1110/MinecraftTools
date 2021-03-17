package dev.benergy10.minecrafttools.utils;

public class TimeConverter {

    public static final int TICKS_PER_SECOND = 20;

    public static long secondsToTicks(double seconds) {
        return Math.round(seconds * TICKS_PER_SECOND);
    }
}
