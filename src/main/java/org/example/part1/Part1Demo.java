package org.example.part1;

public class Part1Demo {

    private static final long HOUR = 60 * 60 * 1000L;

    public static void main(String[] args) {
        DeliveryService service = new DeliveryService();

        // Add drivers with hourly rates
        service.addDriver("D1", 15.00);  // $15/hour
        service.addDriver("D2", 20.00);  // $20/hour
        service.addDriver("D3", 12.50);  // $12.50/hour

        // Add deliveries (startTime, endTime) - cost calculated from hourly rate
        service.addDelivery("D1", 0, 2 * HOUR);           // 2 hours * $15 = $30
        service.addDelivery("D1", 3 * HOUR, 4 * HOUR);    // 1 hour * $15 = $15
        service.addDelivery("D2", 1 * HOUR, 3 * HOUR);    // 2 hours * $20 = $40
        service.addDelivery("D3", 0, 2 * HOUR);           // 2 hours * $12.50 = $25

        // Get total cost - O(1) operation
        System.out.println("Total Cost: " + service.getTotalCost()); // Expected: 110

        // Additional info
        System.out.println("Total drivers: " + service.getDriverCount());
        System.out.println("D1 hourly rate: " + service.getDriver("D1").getHourlyRate());
    }
}
