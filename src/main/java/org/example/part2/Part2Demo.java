package org.example.part2;

import java.math.BigDecimal;

public class Part2Demo {

    public static void main(String[] args) {
        DeliveryService service = new DeliveryService();

        // Add drivers
        service.addDriver("D1");
        service.addDriver("D2");

        // Add deliveries (startTime, endTime, cost)
        service.addDelivery("D1", 0, 30, 30.50);    // ends at 30
        service.addDelivery("D1", 45, 60, 15.25);   // ends at 60
        service.addDelivery("D2", 10, 50, 40.75);   // ends at 50
        service.addDelivery("D2", 70, 100, 30.00);  // ends at 100

        System.out.println("=== Initial State ===");
        System.out.println("Total Cost: " + service.getTotalCost());           // 115
        System.out.println("Cost to be Paid: " + service.getCostToBePaid());   // 115

        System.out.println("\n=== Pay up to time 50 ===");
        BigDecimal paid1 = service.payUpToTime(50);  // Settles: D1(0-30), D2(10-50)
        System.out.println("Amount Paid: " + paid1);
        System.out.println("Cost to be Paid: " + service.getCostToBePaid());   // 45

        System.out.println("\n=== Pay up to time 60 ===");
        BigDecimal paid2 = service.payUpToTime(60);  // Settles: D1(45-60)
        System.out.println("Amount Paid: " + paid2);
        System.out.println("Cost to be Paid: " + service.getCostToBePaid());   // 30

        System.out.println("\n=== Pay up to time 100 ===");
        BigDecimal paid3 = service.payUpToTime(100); // Settles: D2(70-100)
        System.out.println("Amount Paid: " + paid3);
        System.out.println("Cost to be Paid: " + service.getCostToBePaid());   // 0

        System.out.println("\n=== Final State ===");
        System.out.println("Total Cost: " + service.getTotalCost());           // 115
        System.out.println("Total Paid: " + service.getPaidCost());            // 115
        System.out.println("Remaining: " + service.getCostToBePaid());         // 0
    }
}
