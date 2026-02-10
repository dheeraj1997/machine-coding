package org.example.part1;

import java.math.BigDecimal;

public class Part1Demo {

    public static void main(String[] args) {
        DeliveryService service = new DeliveryService();

        // Add drivers
        service.addDriver("D1");
        service.addDriver("D2");
        service.addDriver("D3");

        // Add deliveries (startTime, endTime, cost)
        service.addDelivery("D1", 0, 30, 30.50);
        service.addDelivery("D1", 45, 60, 15.25);
        service.addDelivery("D2", 10, 50, 40.75);
        service.addDelivery("D3", 5, 25, 20.00);

        // Get total cost - O(1) operation
        System.out.println("Total Cost: " + service.getTotalCost()); // Expected: 105

        // Additional info
        System.out.println("Driver D1 deliveries: " + service.getDeliveryCountForDriver("D1"));
        System.out.println("Total drivers: " + service.getDriverCount());
        System.out.println("Total deliveries: " + service.getTotalDeliveryCount());
    }
}
