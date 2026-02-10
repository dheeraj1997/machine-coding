package org.example.part1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeliveryService {

    private final Map<String, Driver> drivers;
    private final List<Delivery> deliveries;
    private BigDecimal totalCost;

    public DeliveryService() {
        this.drivers = new HashMap<>();
        this.deliveries = new ArrayList<>();
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

    public void addDelivery(String driverId, long startTime, long endTime, double cost) {
        if (!drivers.containsKey(driverId)) {
            throw new IllegalArgumentException("Driver not found: " + driverId);
        }
        if (startTime < 0 || endTime < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }
        if (endTime < startTime) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }

        BigDecimal normalizedCost = BigDecimal.valueOf(cost).setScale(2, RoundingMode.HALF_UP);
        Delivery delivery = new Delivery(driverId, startTime, endTime, normalizedCost);
        deliveries.add(delivery);
        totalCost = totalCost.add(normalizedCost).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalCost() {
        return totalCost;
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
