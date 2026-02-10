package org.example.part3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryServiceTest {

    private static final long HOUR = 60 * 60 * 1000L;

    @Nested
    @DisplayName("getMaxSimultaneousDeliveries - Window Edge Cases")
    class MaxSimultaneousDeliveriesTests {

        private DeliveryService service;

        @BeforeEach
        void setUp() {
            service = new DeliveryService();
            service.addDriver("D1", 10.0);
            service.addDriver("D2", 10.0);
            service.addDriver("D3", 10.0);
        }

        @Test
        @DisplayName("Case 1: Delivery starts before window, ends inside window")
        void deliveryStartsBeforeWindowEndsInside() {
            // Window at hour 30 = [6, 30], delivery [4, 7] overlaps [6, 7]
            service.addDelivery("D1", 4 * HOUR, 7 * HOUR);
            
            int result = service.getMaxSimultaneousDeliveries(30 * HOUR);
            
            assertEquals(1, result);
        }

        @Test
        @DisplayName("Case 2: Delivery spans from before window start to inside window")
        void deliverySpansWindowStart() {
            // Window at hour 48 = [24, 48], delivery [22, 25] overlaps [24, 25]
            service.addDelivery("D1", 22 * HOUR, 25 * HOUR);
            
            int result = service.getMaxSimultaneousDeliveries(48 * HOUR);
            
            assertEquals(1, result);
        }

        @Test
        @DisplayName("Case 3: Delivery starts inside window, ends after window")
        void deliveryStartsInsideEndsAfterWindow() {
            // Window at hour 24 = [0, 24], delivery [23, 26] overlaps [23, 24]
            service.addDelivery("D1", 23 * HOUR, 26 * HOUR);
            
            int result = service.getMaxSimultaneousDeliveries(24 * HOUR);
            
            assertEquals(1, result);
        }

        @Test
        @DisplayName("Case 4: Delivery ends exactly at windowStart - should NOT count")
        void deliveryEndsExactlyAtWindowStart() {
            // Window at hour 30 = [6, 30], delivery [3, 6] ends exactly at windowStart
            service.addDelivery("D1", 3 * HOUR, 6 * HOUR);
            
            int result = service.getMaxSimultaneousDeliveries(30 * HOUR);
            
            assertEquals(0, result);
        }

        @Test
        @DisplayName("Case 5: Delivery starts exactly at windowEnd - should NOT count")
        void deliveryStartsExactlyAtWindowEnd() {
            // Window at hour 24 = [0, 24], delivery [24, 27] starts exactly at windowEnd
            service.addDelivery("D1", 24 * HOUR, 27 * HOUR);
            
            int result = service.getMaxSimultaneousDeliveries(24 * HOUR);
            
            assertEquals(0, result);
        }

        @Test
        @DisplayName("Case 6: Multiple deliveries with identical start/end times")
        void multipleDeliveriesWithIdenticalTimes() {
            service.addDelivery("D1", 10 * HOUR, 13 * HOUR);
            service.addDelivery("D2", 10 * HOUR, 13 * HOUR);
            service.addDelivery("D3", 10 * HOUR, 13 * HOUR);
            
            int result = service.getMaxSimultaneousDeliveries(24 * HOUR);
            
            assertEquals(3, result);
        }

        @Test
        @DisplayName("Case 7: Back-to-back deliveries - no overlap")
        void backToBackDeliveriesNoOverlap() {
            service.addDelivery("D1", 5 * HOUR, 8 * HOUR);
            service.addDelivery("D2", 8 * HOUR, 11 * HOUR);   // starts exactly when D1 ends
            service.addDelivery("D3", 11 * HOUR, 14 * HOUR);  // starts exactly when D2 ends
            
            int result = service.getMaxSimultaneousDeliveries(24 * HOUR);
            
            assertEquals(1, result);
        }

        @Test
        @DisplayName("Case 8: Empty window - no deliveries in range")
        void emptyWindowNoDeliveriesInRange() {
            service.addDelivery("D1", 50 * HOUR, 53 * HOUR);  // delivery at hour 50-53
            
            int result = service.getMaxSimultaneousDeliveries(24 * HOUR);
            
            assertEquals(0, result);
        }

        @Test
        @DisplayName("Case 9: Minimal overlap - 1ms inside window")
        void minimalOverlapOneMillisecond() {
            // Window at hour 30 = [6h, 30h], delivery ends at 6h + 1ms
            service.addDelivery("D1", 4 * HOUR, 6 * HOUR + 1);
            
            int result = service.getMaxSimultaneousDeliveries(30 * HOUR);
            
            assertEquals(1, result);
        }

        @Test
        @DisplayName("Case 10: Peak concurrency at window boundary")
        void peakConcurrencyAtWindowBoundary() {
            // Window at hour 24 = [0, 24], all deliveries overlap at hour 23
            service.addDelivery("D1", 22 * HOUR, 24 * HOUR);
            service.addDelivery("D2", 21 * HOUR, 24 * HOUR);
            service.addDelivery("D3", 23 * HOUR, 24 * HOUR);
            
            int result = service.getMaxSimultaneousDeliveries(24 * HOUR);
            
            assertEquals(3, result);
        }

        @Test
        @DisplayName("Case 11: No drivers/deliveries - empty service")
        void emptyService() {
            DeliveryService emptyService = new DeliveryService();
            
            int result = emptyService.getMaxSimultaneousDeliveries(24 * HOUR);
            
            assertEquals(0, result);
        }

        @Test
        @DisplayName("Case 12: Overlapping deliveries with staggered times")
        void overlappingDeliveriesStaggeredTimes() {
            // D1: [5-8], D2: [6-9], D3: [7-10] -> max overlap at hour 7-8 = 3
            service.addDelivery("D1", 5 * HOUR, 8 * HOUR);
            service.addDelivery("D2", 6 * HOUR, 9 * HOUR);
            service.addDelivery("D3", 7 * HOUR, 10 * HOUR);
            
            int result = service.getMaxSimultaneousDeliveries(24 * HOUR);
            
            assertEquals(3, result);
        }

        @Test
        @DisplayName("Case 13: Window slides to exclude earlier deliveries")
        void windowSlidesToExcludeEarlierDeliveries() {
            // Deliveries at hour 5-8 and 30-33
            service.addDelivery("D1", 5 * HOUR, 8 * HOUR);
            service.addDelivery("D2", 30 * HOUR, 33 * HOUR);
            
            // Window at hour 36 = [12, 36] - only delivery [30-33] is in range
            int result = service.getMaxSimultaneousDeliveries(36 * HOUR);
            
            assertEquals(1, result);
        }
    }
}
