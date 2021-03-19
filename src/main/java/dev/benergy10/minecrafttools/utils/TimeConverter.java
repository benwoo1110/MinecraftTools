package dev.benergy10.minecrafttools.utils;

public final class TimeConverter {

    public static long secondsToTicks(double seconds) {
        return convertNormalise(seconds, Units.SECOND, Units.TICK);
    }

    public static double convert(double duration, Units from, Units to) {
        return duration * from.getMilliseconds() / to.getMilliseconds();
    }

    public static long convertNormalise(double duration, Units from, Units to) {
        return Math.round(convert(duration, from, to));
    }

    public enum Units {
        MILLISECOND(1),
        TICK(50),
        SECOND(1000),
        MINUTE(60000);

        private final int milliseconds;

        Units(int milliseconds) {
            this.milliseconds = milliseconds;
        }

        public int getMilliseconds() {
            return milliseconds;
        }
    }
}
