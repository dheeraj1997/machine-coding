package org.example.part3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class DeliveryService {

    private static final long MILLIS_PER_HOUR = 60 * 60 * 1000L;
    private static final long MAX_DELIVERY_DURATION = 3;
    private static final long MAX_DELIVERY_DURATION_MILLIS = MAX_DELIVERY_DURATION * MILLIS_PER_HOUR;
    private static final long TWENTY_FOUR_HOURS_MILLIS = 24 * MILLIS_PER_HOUR;

    private final Map<String, Driver> drivers;
    private final List<Delivery> deliveries;
    private BigDecimal totalCost;
    private BigDecimal paidCost;

    public DeliveryService() {
        this.drivers = new HashMap<>();
        this.deliveries = new ArrayList<>();
        this.totalCost = BigDecimal.ZERO;
        this.paidCost = BigDecimal.ZERO;
    }

    public void addDriver(String driverId, double hourlyRate) {
        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException("Driver ID cannot be null or empty");
        }
        if (drivers.containsKey(driverId)) {
            throw new IllegalArgumentException("Driver already exists: " + driverId);
        }
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }
        BigDecimal rate = BigDecimal.valueOf(hourlyRate).setScale(2, RoundingMode.HALF_UP);
        drivers.put(driverId, new Driver(driverId, rate));
    }

    public void addDelivery(String driverId, long startTime, long endTime) {
        if (!drivers.containsKey(driverId)) {
            throw new IllegalArgumentException("Driver not found: " + driverId);
        }
        if (startTime < 0 || endTime < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }
        if (endTime < startTime) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
        long durationMillis = endTime - startTime;
        if (durationMillis > MAX_DELIVERY_DURATION_MILLIS) {
            throw new IllegalArgumentException("Delivery duration cannot exceed " + MAX_DELIVERY_DURATION + " hours");
        }

        Driver driver = drivers.get(driverId);
        BigDecimal durationHours = BigDecimal.valueOf(durationMillis)
                .divide(BigDecimal.valueOf(MILLIS_PER_HOUR), 10, RoundingMode.HALF_UP);
        BigDecimal cost = driver.getHourlyRate().multiply(durationHours).setScale(2, RoundingMode.HALF_UP);

        Delivery delivery = new Delivery(driverId, startTime, endTime, cost);
        deliveries.add(delivery);
        totalCost = totalCost.add(cost).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public BigDecimal payUpToTime(long upToTime) {
        BigDecimal amountPaid = BigDecimal.ZERO;

        for (Delivery delivery : deliveries) {
            if (!delivery.isPaid() && delivery.getEndTime() <= upToTime) {
                amountPaid = amountPaid.add(delivery.getCost());
                delivery.markPaid();
            }
        }

        paidCost = paidCost.add(amountPaid).setScale(2, RoundingMode.HALF_UP);
        return amountPaid.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getCostToBePaid() {
        return totalCost.subtract(paidCost).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getPaidCost() {
        return paidCost;
    }

    public int getMaxSimultaneousDeliveries(long timestamp) {
        long windowStart = timestamp - TWENTY_FOUR_HOURS_MILLIS;
        long windowEnd = timestamp;

        List<long[]> events = new ArrayList<>();

        for (Delivery delivery : deliveries) {
            if (delivery.getStartTime() < windowEnd && delivery.getEndTime() > windowStart) {
                long effectiveStart = Math.max(delivery.getStartTime(), windowStart);
                long effectiveEnd = Math.min(delivery.getEndTime(), windowEnd);

                events.add(new long[]{effectiveStart, 1});
                events.add(new long[]{effectiveEnd, -1});
            }
        }

        events.sort((a, b) -> {
            if (a[0] != b[0]) return Long.compare(a[0], b[0]);
            return Long.compare(a[1], b[1]);
        });

        int maxConcurrent = 0;
        int current = 0;

        for (long[] event : events) {
            current += (int) event[1];
            maxConcurrent = Math.max(maxConcurrent, current);
        }

        return maxConcurrent;
    }

    public List<Delivery> getDeliveriesAtTime(long time) {
        return deliveries.stream()
                .filter(d -> d.getStartTime() <= time && time < d.getEndTime())
                .collect(Collectors.toList());
    }

    public List<long[]> getSimultaneousDeliveriesTimeline() {
        List<long[]> events = new ArrayList<>();

        for (Delivery delivery : deliveries) {
            events.add(new long[]{delivery.getStartTime(), 1});
            events.add(new long[]{delivery.getEndTime(), -1});
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

    public int getTotalDeliveryCount() {
        return deliveries.size();
    }
}
