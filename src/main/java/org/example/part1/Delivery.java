package org.example.part1;

import java.math.BigDecimal;

public class Delivery {

    private final long startTime;
    private final long endTime;
    private final BigDecimal cost;

    public Delivery(long startTime, long endTime, BigDecimal cost) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public long getDuration() {
        return endTime - startTime;
    }
}
