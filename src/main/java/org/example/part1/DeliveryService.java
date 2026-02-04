package org.example.part1;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DeliveryService {

    private final Map<String, Driver> drivers;
    private BigDecimal totalCost;

    public DeliveryService() {
        this.drivers = new HashMap<>();
        this.totalCost = BigDecimal.ZERO;
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

        // Compute cost at insertion time for O(1) getTotalCost
        totalCost = totalCost.add(cost);
    }

    public BigDecimal getTotalCost() {
        // O(1) - cost is pre-computed during addDelivery
        return totalCost;
    }

    public Driver getDriver(String driverId) {
        return drivers.get(driverId);
    }

    public int getDriverCount() {
        return drivers.size();
    }
}
