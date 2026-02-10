package org.example.part2;

import java.math.BigDecimal;

public class Part2Demo {

    private static final long HOUR = 60 * 60 * 1000L;

    public static void main(String[] args) {
        DeliveryService service = new DeliveryService();

        // Add drivers with hourly rates
        service.addDriver("D1", 15.00);  // $15/hour
        service.addDriver("D2", 20.00);  // $20/hour

        // Add deliveries - cost calculated from hourly rate * duration
        service.addDelivery("D1", 0, 2 * HOUR);           // 2h * $15 = $30, ends at hour 2
        service.addDelivery("D1", 3 * HOUR, 4 * HOUR);    // 1h * $15 = $15, ends at hour 4
        service.addDelivery("D2", 1 * HOUR, 3 * HOUR);    // 2h * $20 = $40, ends at hour 3
        service.addDelivery("D2", 5 * HOUR, 7 * HOUR);    // 2h * $20 = $40, ends at hour 7

        System.out.println("=== Initial State ===");
        System.out.println("Total Cost: " + service.getTotalCost());           // $125
        System.out.println("Cost to be Paid: " + service.getCostToBePaid());

        System.out.println("\n=== Pay up to hour 3 ===");
        BigDecimal paid1 = service.payUpToTime(3 * HOUR);  // Settles: D1(0-2h)=$30, D2(1-3h)=$40
        System.out.println("Amount Paid: " + paid1);       // $70
        System.out.println("Cost to be Paid: " + service.getCostToBePaid());

        System.out.println("\n=== Pay up to hour 5 ===");
        BigDecimal paid2 = service.payUpToTime(5 * HOUR);  // Settles: D1(3-4h)=$15
        System.out.println("Amount Paid: " + paid2);       // $15
        System.out.println("Cost to be Paid: " + service.getCostToBePaid());

        System.out.println("\n=== Pay up to hour 10 ===");
        BigDecimal paid3 = service.payUpToTime(10 * HOUR); // Settles: D2(5-7h)=$40
        System.out.println("Amount Paid: " + paid3);       // $40
        System.out.println("Cost to be Paid: " + service.getCostToBePaid());   // $0

        System.out.println("\n=== Final State ===");
        System.out.println("Total Cost: " + service.getTotalCost());
        System.out.println("Total Paid: " + service.getPaidCost());
        System.out.println("Remaining: " + service.getCostToBePaid());
    }
}
