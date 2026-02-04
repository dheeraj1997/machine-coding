package org.example.part2;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

        // Compute cost at insertion time for O(1) getTotalCost
        totalCost = totalCost.add(cost);
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * Settle payment for all deliveries that ended on or before upToTime.
     * Returns the amount paid in this settlement.
     */
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

    /**
     * Get remaining delivery costs that haven't been settled yet.
     * O(1) operation.
     */
    public BigDecimal getCostToBePaid() {
        return totalCost.subtract(paidCost);
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
