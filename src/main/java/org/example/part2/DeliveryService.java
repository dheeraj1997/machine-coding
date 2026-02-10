package org.example.part2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class DeliveryService {

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

    private static final long MILLIS_PER_HOUR = 60 * 60 * 1000L;

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

        Driver driver = drivers.get(driverId);
        BigDecimal durationHours = BigDecimal.valueOf(endTime - startTime)
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

    public Driver getDriver(String driverId) {
        return drivers.get(driverId);
    }

    public int getDriverCount() {
        return drivers.size();
    }
}
