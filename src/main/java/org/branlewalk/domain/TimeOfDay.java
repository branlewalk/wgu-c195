package org.branlewalk.domain;

public class TimeOfDay {

    private String label;
    private long millisSinceMidnight;

    public TimeOfDay(String label, long millisSinceMidnight) {
        this.label = label;
        this.millisSinceMidnight = millisSinceMidnight;
    }

    @Override
    public String toString() {
        return label;
    }

    public long getMillisSinceMidnight() {
        return millisSinceMidnight;
    }
}
