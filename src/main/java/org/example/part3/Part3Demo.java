package org.example.part3;

import java.math.BigDecimal;

public class Part3Demo {

    private static final long HOUR = 60 * 60 * 1000L;

    public static void main(String[] args) {
        DeliveryService service = new DeliveryService();

        // Add drivers with hourly rates
        service.addDriver("D1", 10.00);  // $10/hour
        service.addDriver("D2", 15.00);  // $15/hour
        service.addDriver("D3", 20.00);  // $20/hour

        // Add deliveries (max 3 hours duration) - cost = hourlyRate * duration
        service.addDelivery("D1", 5 * HOUR, 8 * HOUR);     // 3h * $10 = $30
        service.addDelivery("D1", 20 * HOUR, 22 * HOUR);   // 2h * $10 = $20
        service.addDelivery("D2", 6 * HOUR, 9 * HOUR);     // 3h * $15 = $45
        service.addDelivery("D2", 21 * HOUR, 23 * HOUR);   // 2h * $15 = $30
        service.addDelivery("D3", 7 * HOUR, 10 * HOUR);    // 3h * $20 = $60
        service.addDelivery("D3", 30 * HOUR, 32 * HOUR);   // 2h * $20 = $40

        /*
         * Timeline visualization (hours):
         * Hour:  5----6----7----8----9----10---...---20---21---22---23---...---30---32
         * D1:    [=========]                        [====]
         * D2:         [=========]                        [=========]
         * D3:              [=========]                                   [====]
         */

        System.out.println("=== Cost Calculation (hourlyRate * duration) ===");
        System.out.println("Total Cost: " + service.getTotalCost());  // $225

        System.out.println("\n=== Max Simultaneous Deliveries (within previous 24 hours) ===");
        System.out.println("At hour 24 (window 0-24): " + service.getMaxSimultaneousDeliveries(24 * HOUR));
        System.out.println("At hour 36 (window 12-36): " + service.getMaxSimultaneousDeliveries(36 * HOUR));

        System.out.println("\n=== Payment ===");
        BigDecimal paid = service.payUpToTime(10 * HOUR);
        System.out.println("Paid up to hour 10: " + paid);
        System.out.println("Remaining to pay: " + service.getCostToBePaid());
    }
}
