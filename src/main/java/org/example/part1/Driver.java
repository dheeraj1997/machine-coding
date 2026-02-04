package org.example.part1;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    private final String driverId;
    private final List<Delivery> deliveries;

    public Driver(String driverId) {
        this.driverId = driverId;
        this.deliveries = new ArrayList<>();
    }

    public String getDriverId() {
        return driverId;
    }

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }

    public List<Delivery> getDeliveries() {
        return new ArrayList<>(deliveries);
    }

    public int getDeliveryCount() {
        return deliveries.size();
    }
}
