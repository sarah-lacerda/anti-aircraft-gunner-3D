package util;

public class Time {
    public static double timeStarted = System.nanoTime();

    private Time() {}

    public static double getCurrentTimeInSeconds() {
        return (System.nanoTime() - timeStarted) * 1E-9;
    }

    public static double deltaTimeInSecondsFrom(double time) {
        return getCurrentTimeInSeconds() - time;
    }

}
