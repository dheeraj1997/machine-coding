package org.example.part3;

import java.math.BigDecimal;

public class Part3Demo {

    private static final long HOUR = 60 * 60 * 1000L; // 1 hour in milliseconds

    public static void main(String[] args) {
        DeliveryService service = new DeliveryService();

        // Add drivers
        service.addDriver("D1");
        service.addDriver("D2");
        service.addDriver("D3");

        // Add deliveries with decimal costs (max 3 hours duration)
        // Timestamps in milliseconds
        service.addDelivery("D1", 5 * HOUR, 8 * HOUR, 25.50);     // D1: hour 5-8
        service.addDelivery("D1", 20 * HOUR, 22 * HOUR, 15.75);   // D1: hour 20-22
        service.addDelivery("D2", 6 * HOUR, 9 * HOUR, 40.25);     // D2: hour 6-9
        service.addDelivery("D2", 21 * HOUR, 23 * HOUR, 30.00);   // D2: hour 21-23
        service.addDelivery("D3", 7 * HOUR, 10 * HOUR, 55.99);    // D3: hour 7-10
        service.addDelivery("D3", 30 * HOUR, 32 * HOUR, 20.00);   // D3: hour 30-32

        /*
         * Timeline visualization (hours):
         * Hour:  5----6----7----8----9----10---...---20---21---22---23---...---30---32
         * D1:    [=========]                        [====]
         * D2:         [=========]                        [=========]
         * D3:              [=========]                                   [====]
         *
         * At hour 24, looking back 24 hours (window: hour 0-24):
         * - D1(5-8), D1(20-22), D2(6-9), D2(21-23), D3(7-10) are in window
         * - Max concurrent at hour 7-8: 3 (D1, D2, D3 overlap)
         *
         * At hour 36, looking back 24 hours (window: hour 12-36):
         * - D1(20-22), D2(21-23), D3(30-32) are in window
         * - Max concurrent at hour 21-22: 2 (D1, D2 overlap)
         */

        System.out.println("=== Decimal Cost Support ===");
        System.out.println("Total Cost: " + service.getTotalCost());

        System.out.println("\n=== Max Simultaneous Deliveries (within previous 24 hours) ===");
        System.out.println("At hour 24 (window 0-24): " + service.getMaxSimultaneousDeliveries(24 * HOUR));
        System.out.println("At hour 36 (window 12-36): " + service.getMaxSimultaneousDeliveries(36 * HOUR));
        System.out.println("At hour 12 (window 0-12): " + service.getMaxSimultaneousDeliveries(12 * HOUR));

        System.out.println("\n=== Deliveries at specific times ===");
        System.out.println("At hour 7: " + service.getDeliveriesAtTime(7 * HOUR));
        System.out.println("At hour 21: " + service.getDeliveriesAtTime(21 * HOUR));
        System.out.println("At hour 31: " + service.getDeliveriesAtTime(31 * HOUR));

        System.out.println("\n=== Payment with decimals ===");
        BigDecimal paid = service.payUpToTime(10 * HOUR);
        System.out.println("Paid up to hour 10: " + paid);
        System.out.println("Remaining to pay: " + service.getCostToBePaid());

        System.out.println("\n=== Timeline of concurrent deliveries ===");
        for (long[] point : service.getSimultaneousDeliveriesTimeline()) {
            System.out.println("Hour " + (point[0] / HOUR) + ": " + point[1] + " concurrent deliveries");
        }
    }
}
