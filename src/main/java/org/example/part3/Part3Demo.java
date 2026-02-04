package org.example.part3;

import java.math.BigDecimal;

public class Part3Demo {

    public static void main(String[] args) {
        DeliveryService service = new DeliveryService();

        // Add drivers
        service.addDriver("D1");
        service.addDriver("D2");
        service.addDriver("D3");

        // Add deliveries with decimal costs
        service.addDelivery("D1", 0, 30, new BigDecimal("25.50"));    // D1: 0-30
        service.addDelivery("D1", 45, 70, new BigDecimal("15.75"));   // D1: 45-70
        service.addDelivery("D2", 10, 50, new BigDecimal("40.25"));   // D2: 10-50
        service.addDelivery("D2", 60, 80, new BigDecimal("30.00"));   // D2: 60-80
        service.addDelivery("D3", 20, 65, new BigDecimal("55.99"));   // D3: 20-65

        /*
         * Timeline visualization:
         * Time:  0----10----20----30----40----50----60----70----80
         * D1:    [==========]          [==============]
         * D2:         [==================]      [==========]
         * D3:              [======================]
         *
         * Concurrent deliveries at different times:
         * t=5:  1 (D1)
         * t=15: 2 (D1, D2)
         * t=25: 3 (D1, D2, D3) <- MAX
         * t=35: 2 (D2, D3)
         * t=55: 2 (D1, D3)
         * t=65: 2 (D1, D2)
         */

        System.out.println("=== Decimal Cost Support ===");
        System.out.println("Total Cost: " + service.getTotalCost());  // 167.49

        System.out.println("\n=== Simultaneous Deliveries ===");
        System.out.println("Max Simultaneous Deliveries: " + service.getMaxSimultaneousDeliveries());  // 3

        System.out.println("\n=== Deliveries at specific times ===");
        System.out.println("At time 25: " + service.getDeliveriesAtTime(25));
        System.out.println("At time 55: " + service.getDeliveriesAtTime(55));
        System.out.println("At time 75: " + service.getDeliveriesAtTime(75));

        System.out.println("\n=== Payment with decimals ===");
        BigDecimal paid = service.payUpToTime(50);
        System.out.println("Paid up to time 50: " + paid);            // 65.75 (D1:25.50 + D2:40.25)
        System.out.println("Remaining to pay: " + service.getCostToBePaid());  // 101.74

        System.out.println("\n=== Timeline of concurrent deliveries ===");
        for (long[] point : service.getSimultaneousDeliveriesTimeline()) {
            System.out.println("Time " + point[0] + ": " + point[1] + " concurrent deliveries");
        }
    }
}
