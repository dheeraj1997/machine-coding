package org.example.part3;

import java.math.BigDecimal;

public class Delivery {

    private final long startTime;
    private final long endTime;
    private final BigDecimal cost;
    private boolean paid;

    public Delivery(long startTime, long endTime, BigDecimal cost) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
        this.paid = false;
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

    public boolean isPaid() {
        return paid;
    }

    public void markPaid() {
        this.paid = true;
    }

    public boolean overlapsWith(Delivery other) {
        return this.startTime < other.endTime && other.startTime < this.endTime;
    }

    @Override
    public String toString() {
        return "Delivery[" + startTime + "-" + endTime + ", cost=" + cost + "]";
    }
}
