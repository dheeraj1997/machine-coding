package org.example.part2;

import java.math.BigDecimal;

public class Driver {

    private final String driverId;
    private final BigDecimal hourlyRate;

    public Driver(String driverId, BigDecimal hourlyRate) {
        this.driverId = driverId;
        this.hourlyRate = hourlyRate;
    }

    public String getDriverId() {
        return driverId;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }
}
