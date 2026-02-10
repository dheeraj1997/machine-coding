package org.example.part3;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

public class Delivery {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    private final long id;
    private final String driverId;
    private final long startTime;
    private final long endTime;
    private final BigDecimal cost;

    public Delivery(String driverId, long startTime, long endTime, BigDecimal cost) {
        this.id = ID_GENERATOR.incrementAndGet();
        this.driverId = driverId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public String getDriverId() {
        return driverId;
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

    public boolean overlapsWith(Delivery other) {
        return this.startTime < other.endTime && other.startTime < this.endTime;
    }

    @Override
    public String toString() {
        return "Delivery[driver=" + driverId + ", " + startTime + "-" + endTime + ", cost=" + cost + "]";
    }
}
