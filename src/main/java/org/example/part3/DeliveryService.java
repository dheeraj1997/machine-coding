package org.example.part3;

import java.math.BigDecimal;
import java.util.*;

public class DeliveryService {

    private final Map<String, Driver> drivers;
    private BigDecimal totalCost;
    private BigDecimal paidCost;

    public DeliveryService() {
        this.drivers = new HashMap<>();
        this.totalCost = BigDecimal.ZERO;
        this.paidCost = BigDecimal.ZERO;
    }

    public void addDriver(String driverId) {
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("Driver ID cannot be null or empty");
        }
        if (drivers.containsKey(driverId)) {
            throw new IllegalArgumentException("Driver already exists: " + driverId);
        }
        drivers.put(driverId, new Driver(driverId));
    }

    public void addDelivery(String driverId, long startTime, long endTime, BigDecimal cost) {
        if (!drivers.containsKey(driverId)) {
            throw new IllegalArgumentException("Driver not found: " + driverId);
        }
        if (startTime < 0 || endTime < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }
        if (endTime < startTime) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
        if (cost == null || cost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cost cannot be null or negative");
        }

        Delivery delivery = new Delivery(startTime, endTime, cost);
        drivers.get(driverId).addDelivery(delivery);

        totalCost = totalCost.add(cost);
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public BigDecimal payUpToTime(long upToTime) {
        BigDecimal amountPaid = BigDecimal.ZERO;

        for (Driver driver : drivers.values()) {
            for (Delivery delivery : driver.getDeliveries()) {
                if (!delivery.isPaid() && delivery.getEndTime() <= upToTime) {
                    amountPaid = amountPaid.add(delivery.getCost());
                    delivery.markPaid();
                }
            }
        }

        paidCost = paidCost.add(amountPaid);
        return amountPaid;
    }

    public BigDecimal getCostToBePaid() {
        return totalCost.subtract(paidCost);
    }

    public BigDecimal getPaidCost() {
        return paidCost;
    }

    /**
     * Get maximum number of simultaneous deliveries happening at any point in time.
     * Uses sweep line algorithm - O(n log n) where n = total deliveries.
     */
    public int getMaxSimultaneousDeliveries() {
        List<int[]> events = new ArrayList<>();

        for (Driver driver : drivers.values()) {
            for (Delivery delivery : driver.getDeliveries()) {
                events.add(new int[]{(int) delivery.getStartTime(), 1});  // +1 at start
                events.add(new int[]{(int) delivery.getEndTime(), -1});   // -1 at end
            }
        }

        // Sort by time; if same time, process ends (-1) before starts (+1)
        events.sort((a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return a[1] - b[1];
        });

        int maxConcurrent = 0;
        int current = 0;

        for (int[] event : events) {
            current += event[1];
            maxConcurrent = Math.max(maxConcurrent, current);
        }

        return maxConcurrent;
    }

    /**
     * Get all deliveries that are active (in progress) at a specific time.
     */
    public List<DeliveryInfo> getDeliveriesAtTime(long time) {
        List<DeliveryInfo> result = new ArrayList<>();

        for (Driver driver : drivers.values()) {
            for (Delivery delivery : driver.getDeliveries()) {
                if (delivery.getStartTime() <= time && time < delivery.getEndTime()) {
                    result.add(new DeliveryInfo(driver.getDriverId(), delivery));
                }
            }
        }

        return result;
    }

    /**
     * Get count of simultaneous deliveries at each point where count changes.
     * Returns a list of [time, count] showing how concurrent deliveries change over time.
     */
    public List<long[]> getSimultaneousDeliveriesTimeline() {
        List<long[]> events = new ArrayList<>();

        for (Driver driver : drivers.values()) {
            for (Delivery delivery : driver.getDeliveries()) {
                events.add(new long[]{delivery.getStartTime(), 1});
                events.add(new long[]{delivery.getEndTime(), -1});
            }
        }

        events.sort((a, b) -> {
            if (a[0] != b[0]) return Long.compare(a[0], b[0]);
            return Long.compare(a[1], b[1]);
        });

        List<long[]> timeline = new ArrayList<>();
        long current = 0;

        for (long[] event : events) {
            current += event[1];
            timeline.add(new long[]{event[0], current});
        }

        return timeline;
    }

    public Driver getDriver(String driverId) {
        return drivers.get(driverId);
    }

    public int getDriverCount() {
        return drivers.size();
    }

    public static class DeliveryInfo {
        private final String driverId;
        private final Delivery delivery;

        public DeliveryInfo(String driverId, Delivery delivery) {
            this.driverId = driverId;
            this.delivery = delivery;
        }

        public String getDriverId() {
            return driverId;
        }

        public Delivery getDelivery() {
            return delivery;
        }

        @Override
        public String toString() {
            return "Driver=" + driverId + ", " + delivery;
        }
    }
}
